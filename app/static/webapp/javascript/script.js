"use strict";
(function() {
  const config = {    
    selector: {
      bank: "#bank",
      state: "#state",
      district: "#district",
      city: "#city",
      branch: "#branch"
    }
  };

  function register_listener() {
    const bank_sel = document
    .querySelector(config.selector.bank);
    // Bank change listener
   bank_sel.addEventListener("change", function(event) {
        event.preventDefault();
        event.target.options[event.target.selectedIndex].dataset.url && window.location.assign(event.target.options[event.target.selectedIndex].dataset.url)
      });
    // State change listener
    document
      .querySelector(config.selector.state)
      .addEventListener("change", function(event) {
        event.preventDefault();        
        event.target.options[event.target.selectedIndex].dataset.url && window.location.assign(event.target.options[event.target.selectedIndex].dataset.url)
      });

    // District change listener
    document
      .querySelector(config.selector.district)
      .addEventListener("change", function(event) {
        event.preventDefault();
        event.target.options[event.target.selectedIndex].dataset.url && window.location.assign(event.target.options[event.target.selectedIndex].dataset.url)
      });

    // City change listener
    document
      .querySelector(config.selector.city)
      .addEventListener("change", function(event) {
        event.preventDefault();
        event.target.options[event.target.selectedIndex].dataset.url && window.location.assign(event.target.options[event.target.selectedIndex].dataset.url)
      });

    // Branch change listener
    document
      .querySelector(config.selector.branch)
      .addEventListener("change", function(event) {
        event.preventDefault();
        event.target.options[event.target.selectedIndex].dataset.url && window.location.assign(event.target.options[event.target.selectedIndex].dataset.url)
      });
  }

  function init() {
    register_listener();
  }  

  document.addEventListener("DOMContentLoaded", () => init());
})();
