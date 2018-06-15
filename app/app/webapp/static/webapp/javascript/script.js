"use strict";
(function() {
  const config = {
    api: {
      state_api: "/api/state?bank_id=",
      district_api: "/api/district?state_id="
    },
    selector: {
      bank: "#bank",
      state: "#state",
      district: "#district"
    }
  };

  function register_listener() {
    // Bank change listener
    document
      .querySelector(config.selector.bank)
      .addEventListener("change", function(event) {
        event.preventDefault();
        console.log(event.target.value);
        load_data(`${config.api.state_api}${event.target.value}`).then(data => {
          update_select_elem(config.selector.state, data);
        });
      });
    // State change listener
    document
      .querySelector(config.selector.state)
      .addEventListener("change", function(event) {
        event.preventDefault();
        console.log(event.target.value);
        load_data(`${config.api.district_api}${event.target.value}`).then(
          data => {
            update_select_elem(config.selector.district, data);
          }
        );
      });
  }

  function init() {
    register_listener();
  }

  function update_select_elem(selector, data) {
    let options = [];
    options.push(`<option value='0'>Select</option>`);
    data.forEach(item => {
      options.push(`<option value="${item._id}">${item.name}</option>`);
    });
    document.querySelector(selector).innerHTML = options.join("");
  }

  async function load_data(url) {
    const response = await fetch(url);
    const data = await response.json();
    return data;
  }

  document.addEventListener("DOMContentLoaded", () => init());
})();
