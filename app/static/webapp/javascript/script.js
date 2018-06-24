"use strict";
(function() {  
  function register_listener() {
    document.querySelectorAll('.form-section form select').forEach((select)=>{
      select.addEventListener("change", function(event) {
        event.preventDefault();
        show_loader();
        event.target.options[event.target.selectedIndex].dataset.url && window.location.assign(event.target.options[event.target.selectedIndex].dataset.url)
      });
    })    
  }
  function show_loader(){
    document.querySelector('.loader').style.display='block';
  }
  function init() {
    register_listener();
  }

  document.addEventListener("DOMContentLoaded", () => init());
})();
