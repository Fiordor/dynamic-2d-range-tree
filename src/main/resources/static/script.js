
var viewer = null;

function add2DRangeTree() {

    let form = document.getElementById('form2DRangeTree');
    let x = parseInt(form.x.value);
    let y = parseInt(form.y.value)

    let list = document.getElementById('listPoints');
    let child = `
        <div class="row-point">
            <div class="point-l">${x}</div>
            <div class="point-t">${y}</div>
        </div>`;
    list.innerHTML = child + list.innerHTML;

    let encodeValues = 'x=' + encodeURIComponent(x) + '&' + 'y=' + encodeURIComponent(y);

    form.reset();

    request('/add-2d-range-tree', encodeValues);
}

function addRedBlackTree() {

    let form = document.getElementById('formRedBlackTree');
    let k = parseInt(form.k.value);

    let list = document.getElementById('listPoints');
    let child = `<div>${k}</div>`;
    list.innerHTML = child + list.innerHTML;

    let encodeValues = 'k=' + encodeURIComponent(k);

    form.reset();
    form.k.focus();

    request('/add-red-black-tree', encodeValues);

}

function request(uri, encodeValues) {

    const xhr = new XMLHttpRequest();
    xhr.open("POST", uri, true);
    xhr.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");

    xhr.onreadystatechange = () => { // Call a function when the state changes.
        if (xhr.readyState === XMLHttpRequest.DONE && xhr.status === 200) {
            // Request finished. Do processing here.
            let res = xhr.responseText;

            if (res.startsWith('data:image')) {

                document.getElementById('image').src = res;

                if (viewer == null) {
                    viewer = new Viewer(document.getElementById('image'), {
                        fullscreen: false,
                        inline: true,
                        navbar: false,
                        title: (image, imageData) => { return `(${imageData.naturalWidth} x ${imageData.naturalHeight})` },
                        toolbar: {
                            zoomIn: 1,
                            zoomOut: 1,
                            oneToOne: 1,
                            reset: 1,
                            prev: 0,
                            play: 0,
                            next: 0,
                            rotateLeft: 0,
                            rotateRight: 0,
                            flipHorizontal: 0,
                            flipVertical: 0,
                        },
                        viewed() { viewer.zoomTo(1); },
                      });
                } else {
                    viewer.update();
                }

            } else {
                alert(res);
            }
        }
    }

    xhr.send(encodeValues);
}

function afterSelection() {
    let option = document.getElementById('treeType').value;
    console.log(option);
    document.getElementById('formRedBlackTree').style.display = option == 'formRedBlackTree' ? 'block' : 'none';
    document.getElementById('form2DRangeTree').style.display = option == 'form2DRangeTree' ? 'block' : 'none';
}