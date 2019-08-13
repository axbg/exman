import { Component, OnInit } from '@angular/core';
import { HttpManagerService } from '../http-manager.service';
import { ToastrService } from 'ngx-toastr';
import { HotkeysService, Hotkey } from 'angular2-hotkeys';

const typingInterval = 300;

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
  currentPage = 1;
  pageOffset = 0;
  totalElements = 0;
  selectedPlatform = "";
  dateDropdown = [];
  selectedDate = "";
  unitFilter = "";
  accountFilter = "";
  amountFilter = "";

  rows: any[] = [];
  filteredRows = this.rows;
  showSpinner: boolean = true;
  typingTimer;

  constructor(private httpManager: HttpManagerService, private toastr: ToastrService,
    private _hotkeysService: HotkeysService) {
    this._hotkeysService.add(new Hotkey('ctrl+s', (event: KeyboardEvent): boolean => {
      this.save();
      return false;
    }));

    this._hotkeysService.add(new Hotkey('ctrl+d', (event: KeyboardEvent): boolean => {
      this.addRow();
      return false;
    }));
  }

  async ngOnInit() {
    await this.loadData("?meta=true");
    this.showSpinner = false;
  }

  async loadData(filters: String) {
    try {
      const result = await this.httpManager.getRequest("/documents" + filters);
      this.rows = <[]>result["rows"];
      this.totalElements = result["pages"];
      this.loadDropdowns(result);
      this.filteredRows = this.rows;
    } catch (err) {
      this.toastr.error(err.error.message);
    }
  }

  loadDropdowns(result) {
    if (result["platforms"] != null && result["dates"] != null) {
      this.platformDropdown = result["platforms"];
      this.platformDropdown.splice(0, 0, "");
      this.dateDropdown = result["dates"];
      this.dateDropdown.splice(0, 0, "");
    }
  }

  async setPage(e) {
    this.currentPage = e.offset + 1;
    await this.applyFilters();
    this.pageOffset = e.offset;
  }

  applyAmountFilter(e) {
    this.amountFilter = e;
    this.currentPage = 1;
    this.pageOffset = 0;
    this.computeTypingDelay();
  }

  applyUnitFilter(e) {
    this.unitFilter = e;
    this.currentPage = 1;
    this.computeTypingDelay();
  }

  applyAccountFilter(e) {
    this.accountFilter = e;
    this.currentPage = 1;
    this.computeTypingDelay();
  }

  applyPlatformFilter(e) {
    this.selectedPlatform = e;
    this.currentPage = 1;
    this.computeTypingDelay();
  }

  applyDateFilter(e) {
    this.selectedDate = e;
    this.currentPage = 1;
    this.computeTypingDelay();
  }

  computeTypingDelay() {
    clearTimeout(this.typingTimer);
    this.typingTimer = setTimeout(() => this.applyFilters(), typingInterval);
  }

  async applyFilters(preserveOffset?: boolean) {
    const filterUrl = this.filterProducer();
    await this.loadData(filterUrl);
    this.selected = [];
    !preserveOffset ? this.pageOffset = 0 : "";
  }

  filterProducer(): String {
    return "?page=" + this.currentPage +
      (this.selectedPlatform != "" ? "&platform=" + encodeURIComponent(this.selectedPlatform) : "") +
      (this.unitFilter != "" ? "&unit=" + this.unitFilter : "") +
      (this.accountFilter != "" ? "&account=" + this.accountFilter : "") +
      (this.selectedDate != "" ? "&date=" + this.selectedDate : "") +
      (this.amountFilter != "" ? "&amount=" + this.amountFilter : "");
  }

  removeFilters() {
    this.selectedPlatform = "";
    this.unitFilter = "";
    this.accountFilter = "";
    this.selectedDate = "";
    this.amountFilter = "";
    this.currentPage = 1;
    this.applyFilters();
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
    } catch (err) {
      this.toastr.error("Insert failed");
    }
  }

  async save() {
    if (this.updated.length !== 0) {
      try {
        const result = await this.httpManager.putRequest("/documents", this.updated);
        this.loadDropdowns(result);
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
        await this.httpManager.putRequest("/documents/delete/rows", { ...selectedIds });
        this.rows = this.rows.filter(row => !selectedIds.includes(row.id));
        this.selected = [];
        this.applyFilters(true);
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

    this.hasUpdates = true;
  }

  async download() {
    const file = await this.httpManager.getBlobRequest("/documents/generate" + this.filterProducer());
    var newBlob = new Blob([file], { type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" });
    const data = window.URL.createObjectURL(newBlob);
    var link = document.createElement('a');
    link.href = data;
    link.download = "filtered_results.xlsx";
    link.click();
  }

}
