package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.Complaint
import com.example.data.model.DailyChartEntry
import com.example.data.model.NoticePost
import com.example.data.model.NoticeReply
import com.example.data.model.PrivateChartEntry
import com.example.data.model.UserProfile
import com.example.data.repository.GolfRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppDestination {
    LOGIN,
    PUBLIC_CHART,
    PRIVATE_CHART,
    DIRECTORY,
    NOTICE_BOARD,
    COMPLAINT_ASSISTANT,
    PROFILE,
    ADMIN_PANEL
}

class GolfViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: GolfRepository
    val todayDateFormatted: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    val todayDisplayDate: String = SimpleDateFormat("EEEE, MMMM dd, yyyy", Locale.getDefault()).format(Date())

    private val _currentDestination = MutableStateFlow(AppDestination.LOGIN)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    private val _currentUser = MutableStateFlow<UserProfile?>(null)
    val currentUser: StateFlow<UserProfile?> = _currentUser.asStateFlow()

    private val _selectedDate = MutableStateFlow(todayDateFormatted)
    val selectedDate: StateFlow<String> = _selectedDate.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _bannerMessage = MutableStateFlow<String?>(null)
    val bannerMessage: StateFlow<String?> = _bannerMessage.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = GolfRepository(
            userDao = db.userDao(),
            dailyChartDao = db.dailyChartDao(),
            privateChartDao = db.privateChartDao(),
            complaintDao = db.complaintDao(),
            noticeBoardDao = db.noticeBoardDao()
        )

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty(todayDateFormatted)
        }
    }

    val allGolfers: StateFlow<List<UserProfile>> = repository.allGolfers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allCaddies: StateFlow<List<UserProfile>> = repository.allCaddies.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allUsers: StateFlow<List<UserProfile>> = repository.allUsers.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val recordedDates: StateFlow<List<String>> = repository.recordedDates.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf(todayDateFormatted)
    )

    val dailyChart: StateFlow<List<DailyChartEntry>> = combine(
        _selectedDate,
        _searchQuery
    ) { date, query ->
        Pair(date, query)
    }.flatMapLatest { (date, query) ->
        if (query.isBlank()) {
            repository.getDailyChart(date)
        } else {
            repository.searchDailyChart(date, query.trim())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allChartEntries: StateFlow<List<DailyChartEntry>> = repository.getAllChartEntries().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val userPlayActivity: StateFlow<List<DailyChartEntry>> = _currentUser.flatMapLatest { user ->
        if (user != null) {
            repository.getEntriesForUser(user.id, user.name)
        } else {
            kotlinx.coroutines.flow.flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val privateCharts: StateFlow<List<PrivateChartEntry>> = _currentUser.flatMapLatest { user ->
        val userId = user?.id ?: ""
        repository.getPrivateCharts(userId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val allComplaints: StateFlow<List<Complaint>> = repository.allComplaints.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val userComplaints: StateFlow<List<Complaint>> = _currentUser.flatMapLatest { user ->
        val userId = user?.id ?: ""
        repository.getUserComplaints(userId)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val noticePosts: StateFlow<List<NoticePost>> = repository.allNoticePosts.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val noticeReplies: StateFlow<List<NoticeReply>> = repository.allNoticeReplies.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun navigateTo(destination: AppDestination) {
        _currentDestination.value = destination
    }

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun clearBanner() {
        _bannerMessage.value = null
    }

    // Login logic
    fun loginWithExistingUser(user: UserProfile) {
        _currentUser.value = user
        _currentDestination.value = AppDestination.PUBLIC_CHART
        _bannerMessage.value = "Welcome back, ${user.name} (${user.role.replaceFirstChar { it.uppercase() }})!"
    }

    fun loginOrRegister(id: String, name: String, role: String, phone: String, address: String, extra: String) {
        viewModelScope.launch {
            val existing = repository.getUserById(id.trim())
            val user = if (existing != null) {
                existing
            } else {
                val newUser = UserProfile(
                    id = id.trim(),
                    name = name.trim(),
                    role = role.lowercase().trim(),
                    phoneNumber = phone.trim(),
                    address = address.trim(),
                    experienceOrHandicap = extra.trim(),
                    registeredDate = todayDateFormatted
                )
                repository.saveUser(newUser)
                newUser
            }
            _currentUser.value = user
            _currentDestination.value = AppDestination.PUBLIC_CHART
            _bannerMessage.value = "Logged in as ${user.name}"
        }
    }

    fun logout() {
        _currentUser.value = null
        _currentDestination.value = AppDestination.LOGIN
        _bannerMessage.value = "You have logged out."
    }

    // Profile updates
    fun updateProfile(name: String, phone: String, address: String, extra: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val updated = user.copy(
                name = name.trim(),
                phoneNumber = phone.trim(),
                address = address.trim(),
                experienceOrHandicap = extra.trim()
            )
            repository.updateUser(updated)
            _currentUser.value = updated
            _bannerMessage.value = "Profile updated successfully!"
        }
    }

    // Daily Chart updates
    fun isSelectedDateToday(): Boolean {
        return _selectedDate.value == todayDateFormatted
    }

    fun updateChartEntry(
        entry: DailyChartEntry,
        newGolferName: String,
        newCaddieName: String,
        newCaddieAadhaar: String,
        newCaddieNumber: String,
        newPhone: String,
        newAddress: String,
        newTimeSlot: String,
        newNotes: String
    ) {
        // Enforce rule: "after one day no edit option only see the chart"
        if (!isSelectedDateToday()) {
            _bannerMessage.value = "Archived chart cannot be edited."
            return
        }

        viewModelScope.launch {
            val updated = entry.copy(
                golferName = newGolferName.trim().ifEmpty { entry.golferName },
                caddieName = newCaddieName.trim().ifEmpty { entry.caddieName },
                caddieAadhaar = newCaddieAadhaar.trim(),
                caddieNumber = newCaddieNumber.trim(),
                phoneNumber = newPhone.trim(),
                address = newAddress.trim(),
                timeSlot = newTimeSlot.trim(),
                notes = newNotes.trim()
            )
            repository.updateChartEntry(updated)
            _bannerMessage.value = "Chart entry #${entry.serialNumber} updated successfully."
        }
    }

    fun finishRound(entry: DailyChartEntry) {
        if (!isSelectedDateToday()) {
            _bannerMessage.value = "Cannot modify archived records."
            return
        }
        viewModelScope.launch {
            repository.markRoundFinished(entry)
            _bannerMessage.value = "Round completed for ${entry.golferName}! Finish time auto-recorded."
        }
    }

    fun addManualPairing(
        golfer: UserProfile,
        caddie: UserProfile,
        caddieAadhaar: String,
        caddieNumber: String,
        timeSlot: String,
        notes: String
    ) {
        viewModelScope.launch {
            val currentEntries = repository.getDailyChart(_selectedDate.value).flatMapLatest {
                kotlinx.coroutines.flow.flowOf(it)
            }
            // Check if caddie already assigned today
            val assignedCaddies = repository.getAvailableCaddiesForDate(_selectedDate.value)
            val isAlreadyAssigned = assignedCaddies.none { it.id == caddie.id }
            if (isAlreadyAssigned) {
                _bannerMessage.value = "${caddie.name} is already assigned on this day!"
                return@launch
            }

            val nextSerial = (dailyChart.value.maxOfOrNull { it.serialNumber } ?: 0) + 1
            val assignedCadNum = if (caddieNumber.isNotBlank()) caddieNumber.trim() else caddie.id.replace("C-", "CAD-#")
            val assignedAadhaar = if (caddieAadhaar.isNotBlank()) caddieAadhaar.trim() else "XXXX-XXXX-${(4120 + (nextSerial * 157) % 5000)}"

            val newEntry = DailyChartEntry(
                date = _selectedDate.value,
                serialNumber = nextSerial,
                golferId = golfer.id,
                golferName = golfer.name,
                caddieId = caddie.id,
                caddieName = caddie.name,
                caddieAadhaar = assignedAadhaar,
                caddieNumber = assignedCadNum,
                phoneNumber = golfer.phoneNumber,
                address = golfer.address,
                timeSlot = timeSlot,
                autoFinishTime = "",
                status = "Active",
                notes = notes
            )
            repository.addChartEntry(newEntry)
            _bannerMessage.value = "Added pairing: ${golfer.name} with caddie ${caddie.name}."
        }
    }

    fun runAutoAssignForDate(date: String) {
        viewModelScope.launch {
            repository.generateDailyChartIfNotExists(date)
            _bannerMessage.value = "Daily random caddie assignments processed for $date."
        }
    }

    // Private Chart operations
    fun addPrivateChartEntry(
        roundTitle: String,
        holesOrCourse: String,
        scoreOrPar: String,
        caddieNotes: String,
        personalTime: String,
        personalNotes: String
    ) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val entry = PrivateChartEntry(
                userId = user.id,
                date = todayDateFormatted,
                roundTitle = roundTitle.trim(),
                holesOrCourse = holesOrCourse.trim(),
                scoreOrPar = scoreOrPar.trim(),
                caddieOrGolferNotes = caddieNotes.trim(),
                privateTime = personalTime.trim(),
                personalNotes = personalNotes.trim()
            )
            repository.addPrivateChart(entry)
            _bannerMessage.value = "Private chart record added!"
        }
    }

    fun deletePrivateChartEntry(entry: PrivateChartEntry) {
        viewModelScope.launch {
            repository.deletePrivateChart(entry)
            _bannerMessage.value = "Private entry removed."
        }
    }

    // Complaint Box operations
    fun submitComplaint(
        targetPersonName: String,
        targetPersonRole: String,
        category: String,
        description: String
    ) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val complaint = Complaint(
                filedByUserId = user.id,
                filedByName = user.name,
                filedByRole = user.role,
                targetPersonName = targetPersonName.trim(),
                targetPersonRole = targetPersonRole,
                category = category,
                description = description.trim(),
                dateFiled = todayDateFormatted
            )
            repository.fileComplaint(complaint)
            _bannerMessage.value = "Complaint filed securely. Only the Admin has access to this report."
        }
    }

    fun adminUpdateComplaint(complaint: Complaint, newStatus: String, adminNotes: String) {
        if (_currentUser.value?.role != "admin") return
        viewModelScope.launch {
            val updated = complaint.copy(
                status = newStatus,
                adminResolutionNotes = adminNotes.trim()
            )
            repository.updateComplaint(updated)
            _bannerMessage.value = "Complaint #${complaint.id} status updated."
        }
    }

    // Notice board
    fun createNoticePost(title: String, message: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val post = NoticePost(
                authorId = user.id,
                authorName = user.name,
                authorRole = user.role,
                title = title.trim(),
                message = message.trim(),
                datePosted = todayDateFormatted
            )
            repository.postNotice(post)
            _bannerMessage.value = "Notice posted successfully."
        }
    }

    fun createNoticeReply(postId: Long, replyText: String) {
        val user = _currentUser.value ?: return
        viewModelScope.launch {
            val reply = NoticeReply(
                postId = postId,
                authorId = user.id,
                authorName = user.name,
                authorRole = user.role,
                replyText = replyText.trim(),
                dateReplied = todayDateFormatted
            )
            repository.replyToNotice(reply)
            _bannerMessage.value = "Reply posted to board."
        }
    }

    // Admin user management
    fun adminAddUser(id: String, name: String, role: String, phone: String, address: String, extra: String) {
        if (_currentUser.value?.role != "admin") return
        viewModelScope.launch {
            val newUser = UserProfile(
                id = id.trim(),
                name = name.trim(),
                role = role.lowercase().trim(),
                phoneNumber = phone.trim(),
                address = address.trim(),
                experienceOrHandicap = extra.trim(),
                registeredDate = todayDateFormatted
            )
            repository.saveUser(newUser)
            _bannerMessage.value = "New member added: ${newUser.name} ($role)"
        }
    }

    fun adminDeleteUser(userId: String) {
        if (_currentUser.value?.role != "admin") return
        viewModelScope.launch {
            repository.deleteUser(userId)
            _bannerMessage.value = "User deleted from registry."
        }
    }
}
