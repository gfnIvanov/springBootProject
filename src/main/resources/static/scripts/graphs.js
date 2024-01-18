import * as d3 from 'https://cdn.jsdelivr.net/npm/d3@7/+esm';

document.addEventListener('DOMContentLoaded', async () => {
    const response = await fetch('http://localhost:9090/get-data-for-graphs');
    const json = await response.json();
    const result = [];
    json.forEach(str => result.push(JSON.parse(str)));
    prepareData(result);
});

const prepareData = function(rawData) {
    const data = [];

    drawDiagramForMovements(rawData);

    const addData = rawData.filter(rd => rd.action === 'additional');
    const addDataGroup = Object.groupBy(addData, ({ name }) => name);
    const delData = rawData.filter(rd => rd.action === 'write-off' || rd.action === 'to-order');
    const delDataGroup = Object.groupBy(delData, ({ name }) => name);

    Object.keys(addDataGroup).forEach(key => {
        addDataGroup[key] = addDataGroup[key][0].quant;
    });

    Object.keys(delDataGroup).forEach(key => {
        delDataGroup[key] = delDataGroup[key].reduce((acc, elem) => {
            return acc + elem.quant
        }, 0);
    });

    Object.keys(addDataGroup).forEach(addKey => {
        data.push({
            name: addKey,
            quant: addDataGroup[addKey] - (delDataGroup[addKey] ? delDataGroup[addKey] : 0)
        });
    });

    drawDiagramForActualQuant(data);
};

const drawDiagramForActualQuant = function(data) {
    // Declare the chart dimensions and margins.
    const width = 600;
    const height = 500;
    const marginTop = 30;
    const marginRight = 0;
    const marginBottom = 30;
    const marginLeft = 40;

    const x = d3.scaleBand()
        .domain(d3.groupSort(data, ([d]) => -d.quant, (d) => d.name))
        .range([marginLeft, width - marginRight])
        .padding(0.1);

    const y = d3.scaleLinear()
        .domain([0, d3.max(data, (d) => d.quant)])
        .range([height - marginBottom, marginTop]);

    const svg = d3.create('svg')
        .attr('width', width)
        .attr('height', height)
        .attr('viewBox', [0, 0, width, height])
        .attr('style', 'max-width: 100%; height: auto;');

    svg.append('g')
        .attr('fill', 'steelblue')
        .selectAll()
        .data(data)
        .join('rect')
        .attr('x', (d) => x(d.name))
        .attr('y', (d) => y(d.quant))
        .attr('height', (d) => y(0) - y(d.quant))
        .attr('width', x.bandwidth());

    svg.append('g')
        .attr('transform', `translate(0,${height - marginBottom})`)
        .call(d3.axisBottom(x).tickSizeOuter(0));

    svg.append('g')
        .attr('transform', `translate(${marginLeft},0)`)
        .call(d3.axisLeft(y).tickFormat((y) => (y).toFixed()))
        .call(g => g.select('.domain').remove())
        .call(g => g.append('text')
            .attr('x', -marginLeft)
            .attr('y', 10)
            .attr('fill', 'currentColor')
            .attr('text-anchor', 'start')
            .text('Актуальное количество товаров по категориям'));

    actualProductQuant.append(svg.node());
};

const drawDiagramForMovements = function(data) {
    // Declare the chart dimensions and margins.
    const width = 650;
    const height = 500;
    const marginTop = 30;
    const marginRight = 0;
    const marginBottom = 30;
    const marginLeft = 40;

    // Declare the x (horizontal position) scale.
    const x = d3.scaleBand()
        .domain(d3.groupSort(data, ([d]) => -d.quant, (d) => d.name)) // descending quant
        .range([marginLeft, width - marginRight])
        .padding(0.1);

    // Declare the y (vertical position) scale.
    const y = d3.scaleLinear()
        .domain([0, d3.max(data, (d) => d.quant)])
        .range([height - marginBottom, marginTop]);

    // Create the SVG container.
    const svg = d3.create('svg')
        .attr('width', width)
        .attr('height', height)
        .attr('viewBox', [0, 0, width, height])
        .attr('style', 'max-width: 100%; height: auto;');

    svg.append('g')
        .attr('fill', 'SeaGreen')
        .selectAll()
        .data(data)
        .join('rect')
        .attr('x', (d) => x(d.name))
        .attr('y', (d) => y(d.quant))
        .attr('height', (d) => {
            if (d.action === 'additional') return y(0) - y(d.quant)
        })
        .attr('width', x.bandwidth());

    svg.append('g')
        .attr('fill', 'GoldenRod')
        .selectAll()
        .data(data)
        .join('rect')
        .attr('x', (d) => x(d.name))
        .attr('y', (d) => y(d.quant))
        .attr('height', (d) => {
            if (d.action === 'to-order') return y(0) - y(d.quant)
        })
        .attr('width', x.bandwidth());

    svg.append('g')
        .attr('fill', 'FireBrick')
        .selectAll()
        .data(data)
        .join('rect')
        .attr('x', (d) => x(d.name))
        .attr('y', (d) => y(d.quant))
        .attr('height', (d) => {
            if (d.action === 'write-off') return y(0) - y(d.quant)
        })
        .attr('width', x.bandwidth());

    // Add the x-axis and label.
    svg.append('g')
        .attr('transform', `translate(0,${height - marginBottom})`)
        .call(d3.axisBottom(x).tickSizeOuter(0));

    // Add the y-axis and label, and remove the domain line.
    svg.append('g')
        .attr('transform', `translate(${marginLeft},0)`)
        .call(d3.axisLeft(y).tickFormat((y) => (y).toFixed()))
        .call(g => g.select('.domain').remove())
        .call(g => g.append('text')
            .attr('x', -marginLeft)
            .attr('y', 10)
            .attr('fill', 'currentColor')
            .attr('text-anchor', 'start')
            .text('Движение товаров по категориям'));
    // Return the SVG element.
    movementsProducts.append(svg.node());

    const legendData = [
        { action: 'Поступило', color: 'SeaGreen' },
        { action: 'Списано', color: 'FireBrick' },
        { action: 'Заказано', color: 'GoldenRod' },
    ];

    const legendTable = d3.select('svg').append('g')
        .attr('transform', `translate(0, 0)`)
        .attr('class', 'legendTable');

    const legend = legendTable.selectAll('.legend')
        .data(legendData)
        .enter().append('g')
        .attr('class', 'legend')
        .attr('transform', (d, i) => {
            return `translate(0, ${i * 20})`;
        });

    legend.append('rect')
        .attr('x', width - 10)
        .attr('y', 4)
        .attr('width', 10)
        .attr('height', 10)
        .style('fill', (d) => d.color);

    legend.append('text')
        .attr('x', width - 16)
        .attr('y', 9)
        .attr('dy', '.35em')
        .attr('fill', 'rgba(255, 255, 255, 0.87)')
        .style('text-anchor', 'end')
        .style('font-size', '10px')
        .text((d) => d.action);
};