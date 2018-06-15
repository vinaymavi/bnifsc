"use strict";
(function(){
    var config = {
        state_api:"/api/state"
    }

    function register_listener(){
        document.querySelector("#bank").addEventListener("change",function(event){
            event.preventDefault();
            console.log(event.target.value);
        });     
    }    
}());