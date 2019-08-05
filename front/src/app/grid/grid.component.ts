import { Component, OnInit } from '@angular/core';
import { HttpManagerService } from '../http-manager.service';
import { ToastrService } from 'ngx-toastr';
import { HotkeysService, Hotkey } from 'angular2-hotkeys';

@Component({
  selector: 'app-grid',
  templateUrl: './grid.component.html',
  styleUrls: ['./grid.component.css']
})
export class GridComponent implements OnInit {

  editing = {};
  selected = [];
  updated = [];
  hasUpdates: boolean = false;

  platformDropdown = [];
  selectedPlatform = "";
  dateDropdown = [];
  selectedDate = "";
  unitFilter = "";
  accountFilter = "";
  amountFilter = "";

  rows: any[] = [];
  filteredRows = this.rows;
  showSpinner: boolean = true;

  constructor(private httpManager: HttpManagerService, private toastr: ToastrService,
    private _hotkeysService: HotkeysService) {
      this._hotkeysService.add(new Hotkey('ctrl+s', (event: KeyboardEvent): boolean => {
        this.save();
        return false; // Prevent bubbling
    }));

      this._hotkeysService.add(new Hotkey('ctrl+d', (event: KeyboardEvent): boolean => {
        this.addRow();
        return false; // Prevent bubbling
    }));
  }

  async ngOnInit() {
    this.loadData();
  }

  async loadData() {
    try {
      const result = await this.httpManager.getRequest("/documents");
      this.rows = <[]>result;
      this.filterPlatform();
      this.filterDate();
      this.filteredRows = this.rows;
      this.showSpinner = false;
    } catch (err) {
      this.toastr.error(err.error.message);
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
    if (this.amountFilter === "" && this.amountFilter && this.amountFilter && this.amountFilter && this.amountFilter) {
      this.filteredRows = this.rows;
    } else {
      this.filteredRows = this.rows.filter(row => row.amount.toString().startsWith(this.amountFilter))
        .filter(row => row.unit.toString().startsWith(this.unitFilter))
        .filter(row => row.account.toString().startsWith(this.accountFilter))
        .filter(row => this.selectedPlatform !== "" ? this.selectedPlatform === row.platform : true)
        .filter(row => this.selectedDate !== "" ? this.selectedDate === row.date : true);
    }
  }

  removeFilters() {
    this.amountFilter = "";
    this.accountFilter = "";
    this.selectedPlatform = "";
    this.selectedDate = "";
    this.unitFilter = "";
  }

  onSelect({ selected }) {
    this.selected.splice(0, this.selected.length);
    this.selected.push(...selected);
  }

  async addRow() {
    try {
      const result = await this.httpManager.postRequest("/documents/row", {});
      this.rows.push(result);
      this.removeFilters();
      this.applyFilters();
      const dt = document.getElementsByTagName('datatable-body')[0];
      setTimeout(() => {
        dt.scrollTo(0, dt.scrollHeight);
        this.toastr.success("Row added");
      }, 10);
    } catch (err) {
      this.toastr.error("Insert failed");
    }
  }

  async save() {
    if(this.updated.length !== 0) {
      try {
        await this.httpManager.putRequest("/documents", this.updated);
        this.toastr.success("Rows updated");
        this.updated = [];
        this.hasUpdates = false;
      } catch (err) {
        this.toastr.error("Rows were not updated");
      }
    }
  }

  async delete() {
    if (this.selected.length !== 0 && confirm("Are you sure you want to delete?")) {
      try {
        const selectedIds = this.selected.map(select => select.id);
        await this.httpManager.deleteRows("/documents/delete/rows", { ...selectedIds });
        this.rows = this.rows.filter(row => !selectedIds.includes(row.id));
        this.selected = [];
        this.applyFilters();
        this.toastr.success("Rows deleted");
      } catch (err) {
        this.toastr.error(err.error.message);
      }
    }
  }

  updateValue(event, cell, rowIndex) {
    this.editing[rowIndex + '-' + cell] = false;

    this.filteredRows[rowIndex][cell] = event.target.value;
    this.filteredRows = [...this.filteredRows];

    let existingRow = this.updated.find(element => element.id === this.filteredRows[rowIndex]["id"]);

    if (existingRow) {
      this.updated[this.updated.indexOf(existingRow)] = this.filteredRows[rowIndex];
    } else {
      this.updated.push(this.filteredRows[rowIndex]);
    }

    if (cell === "platform") {
      this.filterPlatform();
    } else if (cell === "date") {
      this.filterDate();
    }

    this.hasUpdates = true;
  }

  refresh() {
    this.showSpinner = true;
    this.removeFilters();
    this.loadData();
  }

}
