// API Base URL
const API_BASE = '/api';

// Current tab
let currentTab = 'calendar';

// Calendar state
let currentCalendarYear = new Date().getFullYear();
let currentCalendarMonth = new Date().getMonth();

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

// Store plan data for progress calculation
let planData = {
    daily: [],
    weekly: [],
    monthly: [],
    yearly: []
};

// Initialize
document.addEventListener('DOMContentLoaded', () => {
    initTabs();
    initYearSelects();
    setDefaultDates();
    initDarkMode();
    loadAllData();
    initCalendar();
});

// Dark Mode
function initDarkMode() {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark' || (!savedTheme && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        document.documentElement.setAttribute('data-theme', 'dark');
        updateThemeIcon(true);
    }
}

function toggleDarkMode() {
    const isDark = document.documentElement.getAttribute('data-theme') === 'dark';
    if (isDark) {
        document.documentElement.removeAttribute('data-theme');
        localStorage.setItem('theme', 'light');
        updateThemeIcon(false);
    } else {
        document.documentElement.setAttribute('data-theme', 'dark');
        localStorage.setItem('theme', 'dark');
        updateThemeIcon(true);
    }
}

function updateThemeIcon(isDark) {
    const icon = document.querySelector('.theme-icon');
    if (icon) {
        icon.textContent = isDark ? '☀️' : '🌙';
    }
}

// Update progress badges
function updateProgressBadges() {
    ['daily', 'weekly', 'monthly', 'yearly'].forEach(type => {
        const data = planData[type];
        const total = data.length;
        const completed = data.filter(p => p.status === 'COMPLETED').length;
        const badge = document.getElementById(`${type}-progress`);
        if (badge && total > 0) {
            badge.textContent = `${completed}/${total}`;
        } else if (badge) {
            badge.textContent = '';
        }
    });
}

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
    ['daily', 'weekly', 'monthly', 'yearly'].forEach(loadPlanData);
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

/**
 * 공통 계획 데이터 로드 함수
 * @param {string} type - 계획 타입 (daily, weekly, monthly, yearly)
 */
async function loadPlanData(type) {
    try {
        const data = await apiCall(`/${type}`);
        planData[type] = data;
        renderPlanList(`${type}-list`, data, type);
        updateProgressBadges();
        // Update calendar dots if calendar is visible
        if (document.getElementById('calendar-days')) {
            renderCalendarDots();
        }
    } catch (error) {
        showToast(error.message, 'error');
    }
}

// Daily
const loadDaily = () => loadPlanData('daily');

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
const loadWeekly = () => loadPlanData('weekly');

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
const loadMonthly = () => loadPlanData('monthly');

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
const loadYearly = () => loadPlanData('yearly');

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
        <div class="plan-card status-${plan.status}" draggable="true" data-id="${plan.id}" data-type="${type}">
            <div class="priority-bar priority-${plan.priority}"></div>
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
                        <button class="btn btn-danger btn-sm" onclick="deletePlanWithAnimation('${type}', ${plan.id}, this)">삭제</button>
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

// Delete plan with animation
async function deletePlanWithAnimation(type, id, buttonElement) {
    if (!confirm('정말 삭제하시겠습니까?')) {
        return;
    }

    const card = buttonElement.closest('.plan-card');

    try {
        await apiCall(`/${type}/${id}`, 'DELETE');

        // Add exit animation
        if (card) {
            card.classList.add('removing');
            await new Promise(resolve => setTimeout(resolve, 300));
        }

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
    if (currentTab === 'calendar') {
        loadAllData();
        loadCalendarSidebar();
    } else {
        loadPlanData(currentTab);
    }
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

// ===== Calendar Functions =====

/**
 * 캘린더 초기화 함수
 * 현재 날짜를 기준으로 캘린더를 렌더링하고 사이드바를 로드한다.
 */
function initCalendar() {
    renderCalendar();
    loadCalendarSidebar();
}

/**
 * 월 이동 네비게이션 함수
 * @param {number} delta - 이동할 월 수 (-1: 이전 달, 1: 다음 달)
 */
function navigateMonth(delta) {
    currentCalendarMonth += delta;
    if (currentCalendarMonth > 11) {
        currentCalendarMonth = 0;
        currentCalendarYear++;
    } else if (currentCalendarMonth < 0) {
        currentCalendarMonth = 11;
        currentCalendarYear--;
    }
    renderCalendar();
    loadCalendarSidebar();
}

/**
 * 오늘 날짜로 이동하는 함수
 */
function goToToday() {
    const today = new Date();
    currentCalendarYear = today.getFullYear();
    currentCalendarMonth = today.getMonth();
    renderCalendar();
    loadCalendarSidebar();
}

/**
 * 캘린더 렌더링 함수
 * 현재 연도/월을 기준으로 달력 그리드를 렌더링한다.
 */
function renderCalendar() {
    const titleEl = document.getElementById('calendar-title');
    const daysEl = document.getElementById('calendar-days');

    // Update title
    const monthNames = ['1월', '2월', '3월', '4월', '5월', '6월',
                        '7월', '8월', '9월', '10월', '11월', '12월'];
    titleEl.textContent = `${currentCalendarYear}년 ${monthNames[currentCalendarMonth]}`;

    // Calculate days
    const firstDay = new Date(currentCalendarYear, currentCalendarMonth, 1);
    const lastDay = new Date(currentCalendarYear, currentCalendarMonth + 1, 0);
    const startDayOfWeek = firstDay.getDay();
    const daysInMonth = lastDay.getDate();

    // Previous month days
    const prevLastDay = new Date(currentCalendarYear, currentCalendarMonth, 0).getDate();

    // Today check
    const today = new Date();
    const todayStr = formatDate(today);

    let html = '';

    // Previous month's trailing days
    for (let i = startDayOfWeek - 1; i >= 0; i--) {
        const day = prevLastDay - i;
        const prevMonth = currentCalendarMonth === 0 ? 11 : currentCalendarMonth - 1;
        const prevYear = currentCalendarMonth === 0 ? currentCalendarYear - 1 : currentCalendarYear;
        const dateStr = `${prevYear}-${String(prevMonth + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        html += createCalendarDay(day, dateStr, true, 0);
    }

    // Current month days
    for (let day = 1; day <= daysInMonth; day++) {
        const dateStr = `${currentCalendarYear}-${String(currentCalendarMonth + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        const dayOfWeek = new Date(currentCalendarYear, currentCalendarMonth, day).getDay();
        const isToday = dateStr === todayStr;
        html += createCalendarDay(day, dateStr, false, dayOfWeek, isToday);
    }

    // Next month's leading days
    const totalCells = Math.ceil((startDayOfWeek + daysInMonth) / 7) * 7;
    const remainingCells = totalCells - (startDayOfWeek + daysInMonth);
    for (let day = 1; day <= remainingCells; day++) {
        const nextMonth = currentCalendarMonth === 11 ? 0 : currentCalendarMonth + 1;
        const nextYear = currentCalendarMonth === 11 ? currentCalendarYear + 1 : currentCalendarYear;
        const dateStr = `${nextYear}-${String(nextMonth + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
        html += createCalendarDay(day, dateStr, true, 0);
    }

    daysEl.innerHTML = html;

    // Render plan dots after DOM is updated
    renderCalendarDots();
}

/**
 * 캘린더 일자 요소를 생성하는 함수
 * @param {number} day - 일자
 * @param {string} dateStr - 날짜 문자열 (YYYY-MM-DD)
 * @param {boolean} isOtherMonth - 다른 달 여부
 * @param {number} dayOfWeek - 요일 (0: 일요일, 6: 토요일)
 * @param {boolean} isToday - 오늘 여부
 * @returns {string} HTML 문자열
 */
function createCalendarDay(day, dateStr, isOtherMonth, dayOfWeek, isToday = false) {
    let classes = 'calendar-day';
    if (isOtherMonth) classes += ' other-month';
    if (isToday) classes += ' today';
    if (dayOfWeek === 0) classes += ' sun';
    if (dayOfWeek === 6) classes += ' sat';

    return `
        <div class="${classes}" data-date="${dateStr}" onclick="onCalendarDayClick('${dateStr}')">
            <div class="day-number">${day}</div>
            <div class="day-dots" id="dots-${dateStr}"></div>
        </div>
    `;
}

/**
 * 캘린더에 계획 점을 렌더링하는 함수
 * 각 일자에 해당하는 계획들을 점으로 표시한다.
 */
function renderCalendarDots() {
    // Clear all dots first
    document.querySelectorAll('.day-dots').forEach(el => el.innerHTML = '');

    // Daily plans - show on planDate
    planData.daily.forEach(plan => {
        const dotsEl = document.getElementById(`dots-${plan.planDate}`);
        if (dotsEl) {
            dotsEl.innerHTML += createPlanDot(plan);
        }
    });

    // Weekly plans - show on weekStartDate
    planData.weekly.forEach(plan => {
        const dotsEl = document.getElementById(`dots-${plan.weekStartDate}`);
        if (dotsEl) {
            dotsEl.innerHTML += createPlanDot(plan);
        }
    });
}

/**
 * 계획 점 HTML을 생성하는 함수
 * @param {Object} plan - 계획 객체
 * @returns {string} HTML 문자열
 */
function createPlanDot(plan) {
    const priorityClass = plan.priority.toLowerCase();
    const completedClass = plan.status === 'COMPLETED' ? ' completed' : '';
    return `<div class="plan-dot ${priorityClass}${completedClass}" title="${escapeHtml(plan.title)}"></div>`;
}

/**
 * 캘린더 일자 클릭 이벤트 핸들러
 * 선택된 날짜로 일간 계획 탭으로 이동하고 필터를 적용한다.
 * @param {string} dateStr - 클릭한 날짜 (YYYY-MM-DD)
 */
function onCalendarDayClick(dateStr) {
    // Switch to daily tab
    switchTab('daily');

    // Set the filter date
    document.getElementById('daily-filter-date').value = dateStr;

    // Apply filter
    filterDaily();
}

/**
 * 캘린더 사이드바에 이번 달 계획 목록을 로드하는 함수
 */
async function loadCalendarSidebar() {
    const year = currentCalendarYear;
    const month = currentCalendarMonth + 1;

    // Calculate month start and end dates
    const startDate = `${year}-${String(month).padStart(2, '0')}-01`;
    const lastDay = new Date(year, month, 0).getDate();
    const endDate = `${year}-${String(month).padStart(2, '0')}-${String(lastDay).padStart(2, '0')}`;

    try {
        // Load daily plans for this month
        const dailyPlans = await apiCall(`/daily/date-range?startDate=${startDate}&endDate=${endDate}`);
        renderSidebarPlanList('sidebar-daily-list', dailyPlans, 'daily');

        // Load weekly plans (filter by week start date in this month range)
        const weeklyPlans = await apiCall(`/weekly/date-range?startDate=${startDate}&endDate=${endDate}`);
        renderSidebarPlanList('sidebar-weekly-list', weeklyPlans, 'weekly');

        // Load monthly plans for this month
        const monthlyPlans = await apiCall(`/monthly/year/${year}/month/${month}`);
        renderSidebarPlanList('sidebar-monthly-list', monthlyPlans, 'monthly');

    } catch (error) {
        console.error('Failed to load calendar sidebar:', error);
    }
}

/**
 * 사이드바 계획 목록을 렌더링하는 함수
 * @param {string} containerId - 컨테이너 요소 ID
 * @param {Array} plans - 계획 배열
 * @param {string} type - 계획 타입
 */
function renderSidebarPlanList(containerId, plans, type) {
    const container = document.getElementById(containerId);

    if (!plans || plans.length === 0) {
        container.innerHTML = '<div class="sidebar-empty">계획 없음</div>';
        return;
    }

    container.innerHTML = plans.map(plan => createSidebarPlanItem(plan, type)).join('');
}

/**
 * 사이드바 계획 아이템 HTML을 생성하는 함수
 * @param {Object} plan - 계획 객체
 * @param {string} type - 계획 타입
 * @returns {string} HTML 문자열
 */
function createSidebarPlanItem(plan, type) {
    let dateInfo = '';
    if (type === 'daily') {
        dateInfo = plan.planDate;
    } else if (type === 'weekly') {
        dateInfo = `${plan.weekStartDate} ~ ${plan.weekEndDate}`;
    } else if (type === 'monthly') {
        dateInfo = `${plan.year}년 ${plan.month}월`;
    }

    const completedClass = plan.status === 'COMPLETED' ? ' completed' : '';

    return `
        <div class="sidebar-plan-item priority-${plan.priority}${completedClass}" onclick="goToPlan('${type}', ${plan.id})">
            <div class="sidebar-plan-info">
                <div class="sidebar-plan-title">${escapeHtml(plan.title)}</div>
                <div class="sidebar-plan-date">${dateInfo}</div>
            </div>
            <span class="sidebar-plan-status ${plan.status}">${STATUS_LABELS[plan.status]}</span>
        </div>
    `;
}

/**
 * 사이드바에서 계획 클릭 시 해당 탭으로 이동하는 함수
 * @param {string} type - 계획 타입
 * @param {number} id - 계획 ID
 */
function goToPlan(type, id) {
    switchTab(type);
    // Optionally highlight the specific plan or open edit modal
    setTimeout(() => {
        const planCard = document.querySelector(`.plan-card[data-id="${id}"][data-type="${type}"]`);
        if (planCard) {
            planCard.scrollIntoView({ behavior: 'smooth', block: 'center' });
            planCard.style.animation = 'none';
            planCard.offsetHeight; // Trigger reflow
            planCard.style.animation = 'cardEnter 0.5s ease';
        }
    }, 100);
}
