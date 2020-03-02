document.addEventListener("DOMContentLoaded", function () {
    const myLuteceUserPickers = document.querySelectorAll('.my_lutece_user_picker');
    myLuteceUserPickers && myLuteceUserPickers.forEach(el => {
        const id = el.getAttribute('id');
        const idMyLuteceUser = id.replace("picker_", "");
        new autoComplete({
            data: {
                src: async () => {
                    // User search query
                    const query = el.value;
                    // Fetch External Data Source
                    const source = await fetch(`http://localhost:8080/habili/rest/mylutece/users/api?keywords=${query}`);
                    data = await source.json();
                    return data.result;
                },
                key: ["title"],
                cache: false
            },
            sort: (a, b) => {
                if (a.match < b.match) return -1;
                if (a.match > b.match) return 1;
                return 0;
            },
            placeHolder: "",
            selector: "#" + id,
            threshold: 3,
            debounce: 300,
            searchEngine: "strict",
            resultsList: {
                render: true,
                container: source => {
                    source.setAttribute("id", "result_list");
                },
                destination: document.querySelector("#" + id),
                position: "afterend",
                element: "ul"
            },
            maxResults: 20,
            highlight: true,
            resultItem: {
                content: (data, source) => {
                    source.innerHTML = data.match;
                },
                element: "li"
            },
            noResults: () => {
                const result = document.createElement("li");
                result.setAttribute("class", "no_result");
                result.setAttribute("tabindex", "1");
                result.innerHTML = "Pas de résultat";
                document.querySelector("#result_list").appendChild(result);
            },
            onSelection: feedback => {
                document.getElementById(id).value = feedback.selection.value.title;
                document.querySelector(`input[name=${idMyLuteceUser}`).value = feedback.selection.value.uid;
                const attributeEls = document.querySelectorAll(`input[searchengine=${id}]`)
                attributeEls && attributeEls.forEach(el => {
                    const attrId = el.getAttribute('myLuteceAttributeId');
                    let value = feedback.selection.value[attrId];
                    if (!value) {
                        value = ''
                    }
                    el.value = value;
                })
            }
        });
    });
});