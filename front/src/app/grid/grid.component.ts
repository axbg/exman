import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css']
})
export class GridComponent implements OnInit {

  editing = {};
  selected = [];
  updated = [];

  platformDropdown = [];
  selectedPlatform = "";
  dateDropdown = [];
  selectedDate = "";
  unitFilter = "";
  accountFilter = "";
  amountFilter = "";

  rows = [
    {
      "id": 1,
      "platform": "MC",
      "unit": 8523,
      "account": 98980989,
      "date": "21/12/2012",
      "amount": 23232.25
    },
    {
      "id": 2,
      "platform": "Platform",
      "unit": 8523,
      "account": 98980989,
      "date": "21/12/2012",
      "amount": 23232.25
    },
    {
      "id": 3,
      "platform": "Platform",
      "unit": 8523,
      "account": 98980989,
      "date": "21/12/2012",
      "amount": 1
    },
    {
      "id": 4,
      "platform": "Platform",
      "unit": 8523,
      "account": 98980989,
      "date": "21/12/2013",
      "amount": 23232.25
    },

  ];
  filteredRows = this.rows;

  constructor() {

  }

  ngOnInit(): void {
    this.filterPlatform();
    this.filterDate();
  }

  updateValue(event, cell, rowIndex) {
    this.editing[rowIndex + '-' + cell] = false;

    this.filteredRows[rowIndex][cell] = event.target.value;
    this.filteredRows = [...this.filteredRows];

    this.updated.push(this.filteredRows[rowIndex]);

    if (cell === "platform") {
      this.filterPlatform();
    } else if (cell === "date") {
      this.filterDate();
    }
  }


  filterPlatform() {
    this.platformDropdown = Array.from(new Set(this.rows.map(row => row.platform)));
    this.platformDropdown.splice(0, 0, "");
  }

  filterDate() {
    this.dateDropdown = Array.from(new Set(this.rows.map(row => row.date)));
    this.dateDropdown.splice(0, 0, "");
  }

    //combine all filters and use a switch based on event target name
  applyAmountFilter(e) {
    this.amountFilter = e;
    this.applyFilters();
  }

  applyUnitFilter(e) {
    this.unitFilter = e;
    this.applyFilters();
  }

  applyAccountFilter(e) {
    this.accountFilter = e;
    this.applyFilters();
  }

  applyPlatformFilter(e) {
    this.selectedPlatform = e;
    this.applyFilters();
  }

  applyDateFilter(e) {
    this.selectedDate = e;
    this.applyFilters();
  }

  applyFilters() {
    //check for special filters on numeric fields like = > <
    this.filteredRows = this.rows.filter(
      row => row.amount.toString().startsWith(this.amountFilter)
    ).filter(row => row.unit.toString().startsWith(this.unitFilter))
      .filter(row => row.account.toString().startsWith(this.accountFilter))
      .filter(row => this.selectedPlatform !== "" ? this.selectedPlatform === row.platform : true)
      .filter(row => this.selectedDate !== "" ? this.selectedDate === row.date : true);
  }

  onSelect({ selected }) {
    this.selected.splice(0, this.selected.length);
    this.selected.push(...selected);
    console.log(selected);
  }

}
