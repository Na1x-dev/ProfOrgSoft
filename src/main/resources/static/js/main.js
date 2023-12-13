function CallPrint(strid) {
    var prtContent = document.querySelector(strid);
    var prtCSS1 = '<style>.table {width: 100%;margin-bottom: 20px;border: 1px solid #dddddd;border-collapse: collapse; }.table th {font-weight: bold;padding: 5px;background: #efefef;border: 1px solid #dddddd;}.table td {border: 1px solid #dddddd;padding: 5px;}</style>';
    var prtCSS2 = '<style>.table th{font-weight: bold;padding: 5px;background: #efefef;border: 1px solid #333333;}</style>';
    var prtCSS3 = '<style>.table td{font-weight: bold;padding: 5px;background: #efefef;border: 1px solid #333333;}</style>';
    var WinPrint = window.open('gg', 'wp', 'left=50,top=50,width=800,height=640,toolbar=0,scrollbars=1,status=0');
    WinPrint.document.write(prtCSS1);
    WinPrint.document.write('<table class="table">');
    WinPrint.document.write(prtContent.innerHTML);
    while (WinPrint.document.querySelector('.open-dialog') != null) {
        WinPrint.document.querySelector('.open-dialog').parentElement.parentElement.remove();
    }
    WinPrint.document.write('</table>');

    WinPrint.document.close();
    WinPrint.focus();
    WinPrint.print();
    WinPrint.close();
    prtContent.innerHTML = strOldOne;
}

let switcherButtonLight = document.querySelector(".switcher-button-light");
let switcherButtonDark = document.querySelector(".switcher-button-dark");


switcherButtonDark.onclick = () => {
    localStorage.isDarkTheme = 'true';
    setTheme();
}

switcherButtonLight.onclick = () => {
    localStorage.isDarkTheme = 'false';
    setTheme();
}

function setTheme() {
    if (localStorage.getItem('isDarkTheme') === 'true') {
        switcherButtonDark.style.backgroundColor = "var(--bg-color-1)";
        switcherButtonLight.style.backgroundColor = "var(--bg-color-2)";
        document.documentElement.style.setProperty('--bg-color-1', '#222');
        document.documentElement.style.setProperty('--bg-color-2', '#333');
        document.documentElement.style.setProperty('--bg-color-3', '#444');
        document.documentElement.style.setProperty('--bg-color-4', '#555');
        document.documentElement.style.setProperty('--font-color-1', '#ddd');
        document.documentElement.style.setProperty('--font-color-2', '#eee');
        document.documentElement.style.setProperty('--font-color-3', '#fff');
        document.documentElement.style.setProperty('--border-color', '#777');
    } else if ((localStorage.getItem('isDarkTheme') === 'false')) {
        switcherButtonLight.style.backgroundColor = "var(--bg-color-1)";
        switcherButtonDark.style.backgroundColor = "var(--bg-color-2)";
        document.documentElement.style.setProperty('--bg-color-1', '#ccc');
        document.documentElement.style.setProperty('--bg-color-2', '#ddd');
        document.documentElement.style.setProperty('--bg-color-3', '#eee');
        document.documentElement.style.setProperty('--bg-color-4', '#fff');
        document.documentElement.style.setProperty('--font-color-1', '#222');
        document.documentElement.style.setProperty('--font-color-2', '#333');
        document.documentElement.style.setProperty('--font-color-3', '#444');
        document.documentElement.style.setProperty('--border-color', '#777');
    }
}

function initLocalStorage() {
    console.log(localStorage.getItem('isDarkTheme'));
    if (localStorage.getItem('isDarkTheme') == null) {
        localStorage.setItem('isDarkTheme', 'false');
    }
}

initLocalStorage();
setTheme();

for (let elem of document.getElementsByTagName('a')) {
    elem.style.transitionDuration = '0.2s';
}

for (let elem of document.getElementsByTagName('button')) {
    elem.style.transitionDuration = '0.2s';
}

function exportToExcel() {
    var table = document.querySelector('.table');
    var headers = [];
    var data = [];

    // Extract headers
    table.querySelectorAll('thead th').forEach(function (header) {
        headers.push(header.innerText);
    });

    // Extract data, excluding columns with "Изменить" or "Удалить"
    table.querySelectorAll('tbody tr').forEach(function (row) {
        var rowData = [];
        row.querySelectorAll('td').forEach(function (cell) {
            var cellText = cell.innerText;
            if (cellText !== "Изменить" && cellText !== "Удалить") {
                rowData.push(cellText);
            }
        });
        data.push(rowData);
    });

    // Create worksheet
    var ws = XLSX.utils.aoa_to_sheet([headers].concat(data));

    // Create workbook
    var wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, "Sheet1");

    // generate XLSX file and save to local file
    XLSX.writeFile(wb, 'exported_data.xlsx');
}

const generate = () => {
    let doc = new Document();

    let paragraph1 = new Paragraph().addRun(new TextRun('Председатель: ').bold().size(28));
    let paragraph2 = new Paragraph().addRun(new TextRun('Секретарь: ').bold().size(28));
    let paragraph3 = new Paragraph().addRun(new TextRun('Присутствовали: ').bold().size(28));
    let paragraph4 = new Paragraph().addRun(new TextRun('ГОЛОСОВАЛИ: ').bold().size(28));


    const textArray = [
        new Paragraph().addRun(new TextRun('Белорусский профессиональный').size(28)),
        new Paragraph().addRun(new TextRun('союз работников связи').size(28)),
        new Paragraph().addRun(new TextRun('').size(28)),
        new Paragraph().addRun(new TextRun('Первичная организация').size(28)),
        new Paragraph().addRun(new TextRun('профсоюза работников').size(28)),
        new Paragraph().addRun(new TextRun('УО «Брестский государственный').size(28)),
        new Paragraph().addRun(new TextRun('колледж связи»').size(28)),
        new Paragraph().addRun(new TextRun('')),
        new Paragraph().addRun(new TextRun('ВЫПИСКА ИЗ ПРОТОКОЛА').bold().size(28)),
        new Paragraph().addRun(new TextRun('')),
        new Paragraph().addRun(new TextRun(document.querySelector('.date-input').value + '                № ' + document.querySelector('.number-input').value).bold().size(28)),
        new Paragraph().addRun(new TextRun('')),
        new Paragraph().addRun(new TextRun('г. Брест').bold().size(28)),
        new Paragraph().addRun(new TextRun('')),
        new Paragraph().addRun(new TextRun('заседания профсоюзного комитета').size(28)),
        new Paragraph().addRun(new TextRun('')),
        paragraph1.addRun(new TextRun('Н.В.Иванюшина').size(28)),
        paragraph2.addRun(new TextRun('' + document.querySelector('.secretary').textContent).size(28)),
        paragraph3.addRun(new TextRun('члены профсоюзного комитета – ' + document.querySelector('.present-people').textContent).size(28)),
        new Paragraph().addRun(new TextRun('Повестка дня:').bold().size(28)),
        new Paragraph().addRun(new TextRun('3. ' + document.querySelector('.title-title-input').value).tab().size(28)),
        new Paragraph().addRun(new TextRun('3. СЛУШАЛИ: ').bold().size(28)),
        new Paragraph().addRun(new TextRun(document.querySelector('.speakers').textContent).size(28)),
        new Paragraph().addRun(new TextRun('РЕШИЛИ: ').bold().size(28)),
        new Paragraph().addRun(new TextRun('    1.  ').bold().size(28)),
        paragraph4.addRun(new TextRun('«за» - '+document.querySelector('.voted-for').value+' чел.,').size(28)),
        new Paragraph().addRun(new TextRun('            «против» - '+document.querySelector('.voted-against').value+' чел.,').size(28)),
        new Paragraph().addRun(new TextRun('            «воздержался» - '+document.querySelector('.abstained').value+' чел.').size(28)),
        new Paragraph().addRun(new TextRun('Верно').size(28)),
        new Paragraph().addRun(new TextRun('Председатель                                               Н.В.Иванюшина').size(28)),
        new Paragraph().addRun(new TextRun(document.querySelector('.date-input').value).size(28))
    ];

    for (let text of textArray) {
        doc.addParagraph(text);
    }

    // doc.addParagraph(paragraph);
    const packer = new Packer();

    packer.toBlob(doc).then(blob => saveAs(blob, "protocol.docx"));
}

const toWordButton = document.querySelector('.to-word-btn');
toWordButton.addEventListener('click', generate);