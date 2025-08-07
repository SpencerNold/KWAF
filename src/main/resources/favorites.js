document.addEventListener("DOMContentLoaded", () => {
    loadPage();
})

async function loadPage() {
    let data = await fetch('/get_favorite_recipes')
        .then((response) => response.json())
        .then((data) => {
            const displayBox = document.querySelector('#display')
            displayBox.innerHTML = ""

            let html = ""
            data.forEach((recipe, index) => { 
                if(index % 4 == 0) {
                    html += `<div class="row py-3">`
                }
                
                html += `
                <div class="col-3 mx-auto">
                        <div class="card p-2" style=width:18rem;>
                            <a href="recipe.html?id=${data[index].id}" class="text-decoration-none text-dark">
                                <div class="card-body d-flex justify-content-between">
                                    <h5 class="card-title mb-0">${data[index].name}</h5>
                                </div>
                                <img src="${data[index].image}" class="card-img">
                            </a>    
                        </div>
                </div>
                `
                
                if(index % 4 == 3) {
                    html += "</div>"
                }
            });
            displayBox.innerHTML = html
        });
}