console.log("This is from custom js");

const toggleSidebar = () => {

    if($(".sidebar").is(":visible")){
        $(".sidebar").css("display", "none");
        $(".content").css("margin-left", "0%");
        $(".ham-icon").css("display", "inline-block");
    } else {
        $(".sidebar").css("display", "block");
        $(".content").css("margin-left", "18%");
        $(".ham-icon").css("display", "none");
    }
};

const search = () => {
    let query = $("#search-input").val();
    if(query == ""){
        $(".search-result").hide();
    }
    else{
        let url = `http://localhost:8282/search/${query}`;

        fetch(url)
            .then((response) => {
                return response.json();
            }).then((data) => {
                console.log(data);
                let text = `<div class="list-group">`;

                data.forEach(contact => {
                    text += `<a href='/user/contact/${contact.cId}' class='list-group-item list-group-action'>${contact.name}</a>`;
                });

                text += `</div>`;

                $(".search-result").html(text);
                $(".search-result").show();
            });
    }
}