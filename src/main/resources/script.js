let searchType = 'title'; // default search type

document.addEventListener('DOMContentLoaded', () => {
    document.querySelector('#submitButton').addEventListener('click', () => search())
    document.querySelectorAll('input[name="searchType"]').forEach((elem) => {
        elem.addEventListener('change', function() {
            searchType = this.id;
        });
    });
})

async function search() {
    const searchInput = document.querySelector('#searchBox').value
    const resultsBox = document.querySelector('#results')

    // Modify the API URL based on the search type
    let apiUrl;
    switch(searchType) {
        case 'title':
            apiUrl = `https://api.spoonacular.com/recipes/complexSearch?apiKey=337e21a72943435b8eb4128acc08555d&query=${searchInput}&number=9`;
            break;
        case 'ingredients':
            apiUrl = `https://api.spoonacular.com/recipes/findByIngredients?apiKey=337e21a72943435b8eb4128acc08555d&ingredients=${searchInput}&number=9`;
            break;
        case 'cuisine':
            apiUrl = `https://api.spoonacular.com/recipes/complexSearch?apiKey=337e21a72943435b8eb4128acc08555d&cuisine=${searchInput}&number=9`;
            break;
        case 'diet':
            apiUrl = `https://api.spoonacular.com/recipes/complexSearch?apiKey=337e21a72943435b8eb4128acc08555d&diet=${searchInput}&number=9`;
            break;
        default:
            apiUrl = `https://api.spoonacular.com/recipes/complexSearch?apiKey=337e21a72943435b8eb4128acc08555d&query=${searchInput}&number=9`;
    }

    // Gets data from api and convert it to JSON
    const response = await fetch(apiUrl)
    const responseJson = await response.json()

    console.log(responseJson);

    let recipes;
    if (searchType === 'title' || searchType === 'cuisine' || searchType === 'diet') {
        recipes = responseJson.results;
    } else if (searchType === 'ingredients') {
        recipes = responseJson;
    }

    // Reset the results box and displays the new results that were just searched for
    resultsBox.innerHTML = ''
    let html = '';
    recipes.forEach((recipe, index) => {
        if (index % 3 === 0) { // if index is divisible by 3 (start of a new row)
            html += '<div class="row">'; // start a new row
        }
        html += `
        <div class="col-4">
        <a href="recipe.html?name=${encodeURIComponent(recipe.title)}&id=${encodeURIComponent(recipe.id)}&image=${encodeURIComponent(recipe.image)}" class="text-decoration-none text-dark">
        <div class="card mb-4">
                    <div class="card-body">
                        <h5 class="card-title">${recipe.title}</h5>
                        <img src="${recipe.image}" class="img-fluid" alt="${recipe.title}">
                    </div>
                </div>
            </a>
        </div>`
        if (index % 3 === 2) { // if index is divisible by 3 with a remainder of 2 (end of a row)
            html += '</div>'; // end the row
        }
    })
    resultsBox.innerHTML = html;
}