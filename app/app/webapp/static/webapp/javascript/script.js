"use strict";
(function() {
  const config = {
    api: {
      state_api: "/api/state?bank_id=",
      district_api: "/api/district?state_id=",
      city_api: "/api/city?district_id=",
      branch_api: "/api/branch?city_id=",
      branch_detail_api: "/api/branch_detail?branch_id="
    },
    selector: {
      bank: "#bank",
      state: "#state",
      district: "#district",
      city: "#city",
      branch: "#branch"
    },
    select_arr: ["#bank", "#state", "#district", "#city", "#branch"]
  };

  function register_listener() {
    // Bank change listener
    document
      .querySelector(config.selector.bank)
      .addEventListener("change", function(event) {
        event.preventDefault();
        console.log(event.target.value);
        clear_next_select_elem(config.selector.bank);
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
        clear_next_select_elem(config.selector.state);
        load_data(`${config.api.district_api}${event.target.value}`).then(
          data => {
            update_select_elem(config.selector.district, data);
          }
        );
      });

    // District change listener
    document
      .querySelector(config.selector.district)
      .addEventListener("change", function(event) {
        event.preventDefault();
        console.log(event.target.value);
        clear_next_select_elem(config.selector.district);
        load_data(`${config.api.city_api}${event.target.value}`).then(data => {
          update_select_elem(config.selector.city, data);
        });
      });

    // City change listener
    document
      .querySelector(config.selector.city)
      .addEventListener("change", function(event) {
        event.preventDefault();
        console.log(event.target.value);
        clear_next_select_elem(config.selector.city);
        load_data(`${config.api.branch_api}${event.target.value}`).then(
          data => {
            update_select_elem(config.selector.branch, data);
          }
        );
      });

    // Branch change listener
    document
      .querySelector(config.selector.branch)
      .addEventListener("change", function(event) {
        event.preventDefault();
        console.log(event.target.value);
        clear_next_select_elem(config.selector.branch);
        load_data(`${config.api.branch_detail_api}${event.target.value}`).then(
          data => {
            update_select_elem(config.selector.branch, data);
          }
        );
      });
  }

  function init() {
    register_listener();
  }

  function clear_next_select_elem(current_elem) {
    const current_index = config.select_arr.indexOf(current_elem);
    const next_elems_arr = config.select_arr.slice(current_index + 1);
    next_elems_arr.length &&
      (document.querySelectorAll(next_elems_arr.join(",")).forEach((node)=>node.innerHTML =
      "<option value='0'>Select</option>"));
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
