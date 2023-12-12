// import {Document, Packer} from "docx";
// import {saveAs} from "file-saver"

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

