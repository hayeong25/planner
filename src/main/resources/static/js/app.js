// API Base URL
const API_BASE = '/api';

// Current tab
let currentTab = 'daily';

// Status and Priority labels
const STATUS_LABELS = {
    'NOT_STARTED': '시작 전',
    'IN_PROGRESS': '진행 중',
    'COMPLETED': '완료',
    'FAILED': '실패'
};

const PRIORITY_LABELS = {
    'HIGH': '높음',
    'MEDIUM': '보통',
    'LOW': '낮음'
};

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    initTabs();
    initYearSelects();
    setDefaultDates();
    loadAllData();
});

// Tab handling
function initTabs() {
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.addEventListener('click', () => {
            const tab = btn.dataset.tab;
            switchTab(tab);
        });
    });
}

function switchTab(tab) {
    currentTab = tab;

    // Update tab buttons
    document.querySelectorAll('.tab-btn').forEach(btn => {
        btn.classList.toggle('active', btn.dataset.tab === tab);
    });

    // Update tab content
    document.querySelectorAll('.tab-content').forEach(content => {
        content.classList.toggle('active', content.id === tab);
    });
}

// Initialize year selects
function initYearSelects() {
    const currentYear = new Date().getFullYear();
    const years = [];
    for (let y = currentYear - 5; y <= currentYear + 5; y++) {
        years.push(y);
    }

    ['monthly-filter-year', 'yearly-filter-year'].forEach(id => {
        const select = document.getElementById(id);
        if (select) {
            years.forEach(year => {
                const option = document.createElement('option');
                option.value = year;
                option.textContent = year + '년';
                select.appendChild(option);
            });
        }
    });
}

// Set default dates
function setDefaultDates() {
    const today = new Date().toISOString().split('T')[0];
    document.getElementById('plan-date').value = today;

    const currentYear = new Date().getFullYear();
    const currentMonth = new Date().getMonth() + 1;
    document.getElementById('month-year').value = currentYear;
    document.getElementById('month-month').value = currentMonth;
    document.getElementById('yearly-year').value = currentYear;

    // Week dates
    const monday = getMonday(new Date());
    const sunday = new Date(monday);
    sunday.setDate(sunday.getDate() + 6);
    document.getElementById('week-start').value = formatDate(monday);
    document.getElementById('week-end').value = formatDate(sunday);
}

function getMonday(date) {
    const d = new Date(date);
    const day = d.getDay();
    const diff = d.getDate() - day + (day === 0 ? -6 : 1);
    return new Date(d.setDate(diff));
}

function formatDate(date) {
    return date.toISOString().split('T')[0];
}

// Load all data
function loadAllData() {
    loadDaily();
    loadWeekly();
    loadMonthly();
    loadYearly();
}

// API calls
async function apiCall(endpoint, method = 'GET', body = null) {
    const options = {
        method,
        headers: {
            'Content-Type': 'application/json'
        }
    };

    if (body) {
        options.body = JSON.stringify(body);
    }

    const response = await fetch(`${API_BASE}${endpoint}`, options);

    if (!response.ok) {
        const error = await response.json().catch(() => ({ message: 'An error occurred' }));
        throw new Error(error.message || 'An error occurred');
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

// Daily
async function loadDaily() {
    try {
        const data = await apiCall('/daily');
        renderPlanList('daily-list', data, 'daily');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function filterDaily() {
    const date = document.getElementById('daily-filter-date').value;
    const status = document.getElementById('daily-filter-status').value;
    const priority = document.getElementById('daily-filter-priority').value;

    try {
        let data;
        if (date) {
            data = await apiCall(`/daily/date/${date}`);
        } else if (status) {
            data = await apiCall(`/daily/status/${status}`);
        } else if (priority) {
            data = await apiCall(`/daily/priority/${priority}`);
        } else {
            data = await apiCall('/daily');
        }
        renderPlanList('daily-list', data, 'daily');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Weekly
async function loadWeekly() {
    try {
        const data = await apiCall('/weekly');
        renderPlanList('weekly-list', data, 'weekly');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function filterWeekly() {
    const date = document.getElementById('weekly-filter-date').value;
    const status = document.getElementById('weekly-filter-status').value;
    const priority = document.getElementById('weekly-filter-priority').value;

    try {
        let data;
        if (date) {
            data = await apiCall(`/weekly/week/${date}`);
        } else if (status) {
            data = await apiCall(`/weekly/status/${status}`);
        } else if (priority) {
            data = await apiCall(`/weekly/priority/${priority}`);
        } else {
            data = await apiCall('/weekly');
        }
        renderPlanList('weekly-list', data, 'weekly');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Monthly
async function loadMonthly() {
    try {
        const data = await apiCall('/monthly');
        renderPlanList('monthly-list', data, 'monthly');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function filterMonthly() {
    const year = document.getElementById('monthly-filter-year').value;
    const month = document.getElementById('monthly-filter-month').value;
    const status = document.getElementById('monthly-filter-status').value;

    try {
        let data;
        if (year && month) {
            data = await apiCall(`/monthly/year/${year}/month/${month}`);
        } else if (year) {
            data = await apiCall(`/monthly/year/${year}`);
        } else if (status) {
            data = await apiCall(`/monthly/status/${status}`);
        } else {
            data = await apiCall('/monthly');
        }
        renderPlanList('monthly-list', data, 'monthly');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Yearly
async function loadYearly() {
    try {
        const data = await apiCall('/yearly');
        renderPlanList('yearly-list', data, 'yearly');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

async function filterYearly() {
    const year = document.getElementById('yearly-filter-year').value;
    const status = document.getElementById('yearly-filter-status').value;
    const priority = document.getElementById('yearly-filter-priority').value;

    try {
        let data;
        if (year) {
            data = await apiCall(`/yearly/year/${year}`);
        } else if (status) {
            data = await apiCall(`/yearly/status/${status}`);
        } else if (priority) {
            data = await apiCall(`/yearly/priority/${priority}`);
        } else {
            data = await apiCall('/yearly');
        }
        renderPlanList('yearly-list', data, 'yearly');
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Reset filters
function resetFilters(type) {
    if (type === 'daily') {
        document.getElementById('daily-filter-date').value = '';
        document.getElementById('daily-filter-status').value = '';
        document.getElementById('daily-filter-priority').value = '';
        loadDaily();
    } else if (type === 'weekly') {
        document.getElementById('weekly-filter-date').value = '';
        document.getElementById('weekly-filter-status').value = '';
        document.getElementById('weekly-filter-priority').value = '';
        loadWeekly();
    } else if (type === 'monthly') {
        document.getElementById('monthly-filter-year').value = '';
        document.getElementById('monthly-filter-month').value = '';
        document.getElementById('monthly-filter-status').value = '';
        loadMonthly();
    } else if (type === 'yearly') {
        document.getElementById('yearly-filter-year').value = '';
        document.getElementById('yearly-filter-status').value = '';
        document.getElementById('yearly-filter-priority').value = '';
        loadYearly();
    }
}

// Drag and drop state
let draggedElement = null;
let draggedType = null;

// Render plan list
function renderPlanList(containerId, plans, type) {
    const container = document.getElementById(containerId);

    if (!plans || plans.length === 0) {
        container.innerHTML = `
            <div class="empty-state">
                <div class="empty-state-icon">📋</div>
                <div class="empty-state-text">등록된 계획이 없습니다.</div>
            </div>
        `;
        return;
    }

    container.innerHTML = plans.map(plan => createPlanCard(plan, type)).join('');
    initDragAndDrop(containerId, type);
}

function createPlanCard(plan, type) {
    let dateInfo = '';
    if (type === 'daily') {
        dateInfo = `📅 ${plan.planDate}`;
    } else if (type === 'weekly') {
        dateInfo = `📅 ${plan.weekStartDate} ~ ${plan.weekEndDate}`;
    } else if (type === 'monthly') {
        dateInfo = `📅 ${plan.year}년 ${plan.month}월`;
    } else if (type === 'yearly') {
        dateInfo = `📅 ${plan.year}년`;
    }

    const isFinalized = plan.status === 'COMPLETED' || plan.status === 'FAILED';
    const statusSelectDisabled = isFinalized ? 'disabled title="완료 또는 실패 상태는 변경할 수 없습니다."' : '';

    return `
        <div class="plan-card" draggable="true" data-id="${plan.id}" data-type="${type}">
            <div class="drag-handle">⋮⋮</div>
            <div class="plan-card-content">
                <div class="plan-card-header">
                    <div>
                        <div class="plan-card-title">${escapeHtml(plan.title)}</div>
                        <div class="plan-card-meta">
                            <span class="badge badge-priority-${plan.priority}">${PRIORITY_LABELS[plan.priority]}</span>
                            <span class="badge badge-status-${plan.status}">${STATUS_LABELS[plan.status]}</span>
                        </div>
                    </div>
                    <select class="status-select" onchange="updateStatus('${type}', ${plan.id}, this.value)" ${statusSelectDisabled}>
                        <option value="NOT_STARTED" ${plan.status === 'NOT_STARTED' ? 'selected' : ''}>시작 전</option>
                        <option value="IN_PROGRESS" ${plan.status === 'IN_PROGRESS' ? 'selected' : ''}>진행 중</option>
                        <option value="COMPLETED" ${plan.status === 'COMPLETED' ? 'selected' : ''}>완료</option>
                        <option value="FAILED" ${plan.status === 'FAILED' ? 'selected' : ''}>실패</option>
                    </select>
                </div>
                ${plan.description ? `<div class="plan-card-description">${escapeHtml(plan.description)}</div>` : ''}
                <div class="plan-card-footer">
                    <div class="plan-card-date">${dateInfo}</div>
                    <div class="plan-card-actions">
                        <button class="btn btn-secondary btn-sm" onclick="editPlan('${type}', ${plan.id})">수정</button>
                        <button class="btn btn-danger btn-sm" onclick="deletePlan('${type}', ${plan.id})">삭제</button>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function escapeHtml(text) {
    const div = document.createElement('div');
    div.textContent = text;
    return div.innerHTML;
}

// Modal handling
function openModal(type, plan = null) {
    const modal = document.getElementById('modal');
    const title = document.getElementById('modal-title');
    const planType = document.getElementById('plan-type');

    // Hide all type-specific fields
    document.querySelectorAll('.type-fields').forEach(el => el.style.display = 'none');

    // Show relevant fields
    document.getElementById(`${type}-fields`).style.display = 'block';

    planType.value = type;

    if (plan) {
        title.textContent = '계획 수정';
        document.getElementById('plan-id').value = plan.id;
        document.getElementById('plan-title').value = plan.title;
        document.getElementById('plan-description').value = plan.description || '';
        document.getElementById('plan-priority').value = plan.priority;
        document.getElementById('plan-status').value = plan.status;

        if (type === 'daily') {
            document.getElementById('plan-date').value = plan.planDate;
        } else if (type === 'weekly') {
            document.getElementById('week-start').value = plan.weekStartDate;
            document.getElementById('week-end').value = plan.weekEndDate;
        } else if (type === 'monthly') {
            document.getElementById('month-year').value = plan.year;
            document.getElementById('month-month').value = plan.month;
        } else if (type === 'yearly') {
            document.getElementById('yearly-year').value = plan.year;
        }
    } else {
        title.textContent = '새 계획';
        document.getElementById('plan-form').reset();
        document.getElementById('plan-id').value = '';
        setDefaultDates();
    }

    modal.classList.add('active');
}

function closeModal() {
    document.getElementById('modal').classList.remove('active');
}

// Save plan
async function savePlan(event) {
    event.preventDefault();

    const type = document.getElementById('plan-type').value;
    const id = document.getElementById('plan-id').value;
    const isEdit = !!id;

    let body = {
        title: document.getElementById('plan-title').value,
        description: document.getElementById('plan-description').value,
        priority: document.getElementById('plan-priority').value,
        status: document.getElementById('plan-status').value
    };

    if (type === 'daily') {
        body.planDate = document.getElementById('plan-date').value;
    } else if (type === 'weekly') {
        body.weekStartDate = document.getElementById('week-start').value;
        body.weekEndDate = document.getElementById('week-end').value;
    } else if (type === 'monthly') {
        body.year = parseInt(document.getElementById('month-year').value);
        body.month = parseInt(document.getElementById('month-month').value);
    } else if (type === 'yearly') {
        body.year = parseInt(document.getElementById('yearly-year').value);
    }

    try {
        if (isEdit) {
            await apiCall(`/${type}/${id}`, 'PUT', body);
            showToast('계획이 수정되었습니다.', 'success');
        } else {
            await apiCall(`/${type}`, 'POST', body);
            showToast('계획이 등록되었습니다.', 'success');
        }

        closeModal();
        reloadCurrentTab();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Edit plan
async function editPlan(type, id) {
    try {
        const plan = await apiCall(`/${type}/${id}`);
        openModal(type, plan);
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Delete plan
async function deletePlan(type, id) {
    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }

    try {
        await apiCall(`/${type}/${id}`, 'DELETE');
        showToast('계획이 삭제되었습니다.', 'success');
        reloadCurrentTab();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Update status
async function updateStatus(type, id, status) {
    try {
        await apiCall(`/${type}/${id}/status`, 'PATCH', { status });
        showToast('상태가 변경되었습니다.', 'success');
        reloadCurrentTab();
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Reload current tab data
function reloadCurrentTab() {
    if (currentTab === 'daily') loadDaily();
    else if (currentTab === 'weekly') loadWeekly();
    else if (currentTab === 'monthly') loadMonthly();
    else if (currentTab === 'yearly') loadYearly();
}

// Toast notification
function showToast(message, type = 'success') {
    const toast = document.getElementById('toast');
    toast.textContent = message;
    toast.className = `toast ${type} show`;

    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

// Close modal on outside click
document.getElementById('modal').addEventListener('click', (e) => {
    if (e.target.id === 'modal') {
        closeModal();
    }
});

// Close modal on Escape key
document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') {
        closeModal();
    }
});

// Drag and Drop functionality
function initDragAndDrop(containerId, type) {
    const container = document.getElementById(containerId);
    const cards = container.querySelectorAll('.plan-card');

    cards.forEach(card => {
        card.addEventListener('dragstart', handleDragStart);
        card.addEventListener('dragend', handleDragEnd);
        card.addEventListener('dragover', handleDragOver);
        card.addEventListener('dragenter', handleDragEnter);
        card.addEventListener('dragleave', handleDragLeave);
        card.addEventListener('drop', handleDrop);
    });
}

function handleDragStart(e) {
    draggedElement = this;
    draggedType = this.dataset.type;
    this.classList.add('dragging');
    e.dataTransfer.effectAllowed = 'move';
    e.dataTransfer.setData('text/plain', this.dataset.id);
}

function handleDragEnd(e) {
    this.classList.remove('dragging');
    document.querySelectorAll('.plan-card').forEach(card => {
        card.classList.remove('drag-over');
    });
    draggedElement = null;
    draggedType = null;
}

function handleDragOver(e) {
    e.preventDefault();
    e.dataTransfer.dropEffect = 'move';
}

function handleDragEnter(e) {
    e.preventDefault();
    if (this !== draggedElement && this.dataset.type === draggedType) {
        this.classList.add('drag-over');
    }
}

function handleDragLeave(e) {
    this.classList.remove('drag-over');
}

async function handleDrop(e) {
    e.preventDefault();
    this.classList.remove('drag-over');

    if (this === draggedElement || this.dataset.type !== draggedType) {
        return;
    }

    const container = this.parentNode;
    const cards = Array.from(container.querySelectorAll('.plan-card'));
    const draggedIndex = cards.indexOf(draggedElement);
    const dropIndex = cards.indexOf(this);

    // Reorder in DOM
    if (draggedIndex < dropIndex) {
        this.parentNode.insertBefore(draggedElement, this.nextSibling);
    } else {
        this.parentNode.insertBefore(draggedElement, this);
    }

    // Get new order and save to server
    const newOrder = Array.from(container.querySelectorAll('.plan-card')).map(card =>
        parseInt(card.dataset.id)
    );

    try {
        await apiCall(`/${draggedType}/reorder`, 'PUT', { orderedIds: newOrder });
        showToast('순서가 변경되었습니다.', 'success');
    } catch (error) {
        showToast(error.message, 'error');
        reloadCurrentTab();
    }
}
