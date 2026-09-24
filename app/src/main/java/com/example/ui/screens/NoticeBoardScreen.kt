package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Forum
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.NoticePost
import com.example.data.model.NoticeReply
import com.example.data.model.UserProfile
import com.example.ui.components.GolfBackground
import com.example.ui.theme.GolfGoldAccent

@Composable
fun NoticeBoardScreen(
    currentUser: UserProfile?,
    posts: List<NoticePost>,
    replies: List<NoticeReply>,
    onCreatePost: (title: String, message: String) -> Unit,
    onReplyToPost: (postId: Long, replyText: String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showCreateDialog by remember { mutableStateOf(false) }

    GolfBackground(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 10.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Campaign,
                            contentDescription = null,
                            tint = GolfGoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Club Notice Board",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Public announcements & interactive community thread",
                        fontSize = 12.sp,
                        color = Color(0xFFA5D6A7)
                    )
                }

                Button(
                    onClick = { showCreateDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("create_notice_btn")
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Color(0xFF1B241C), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Post", color = Color(0xFF1B241C), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Surface(
                color = Color(0x77000000),
                shape = RoundedCornerShape(10.dp),
                border = BorderStroke(1.dp, Color(0x3381C784)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "All golfers, caddies, and staff can view announcements and post replies.",
                    color = Color(0xCCFFFFFF),
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Posts List
            if (posts.isEmpty()) {
                Box(
                    modifier = Modifier.weight(1f).fillMaxWidth().padding(top = 40.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Forum, contentDescription = null, tint = Color(0x6681C784), modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No notices posted yet", color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("Tap 'Post' to share updates with all golfers and caddies.", color = Color(0xAAFFFFFF), fontSize = 12.sp)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .testTag("notices_list"),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(posts, key = { it.id }) { post ->
                        val postReplies = replies.filter { it.postId == post.id }
                        NoticePostCard(
                            post = post,
                            replies = postReplies,
                            onReply = { text -> onReplyToPost(post.id, text) }
                        )
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        CreateNoticeDialog(
            onDismiss = { showCreateDialog = false },
            onPost = { title, msg ->
                onCreatePost(title, msg)
                showCreateDialog = false
            }
        )
    }
}

@Composable
fun NoticePostCard(
    post: NoticePost,
    replies: List<NoticeReply>,
    onReply: (String) -> Unit
) {
    var replyText by remember { mutableStateOf("") }
    var showReplyInput by remember { mutableStateOf(false) }

    Card(
        colors = CardDefaults.cardColors(containerColor = Color(0xEE0B1F13)),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, if (post.isPinned) GolfGoldAccent else Color(0x3381C784)),
        modifier = Modifier.fillMaxWidth().testTag("notice_card_${post.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (post.isPinned) {
                        Icon(Icons.Default.PushPin, contentDescription = "Pinned", tint = GolfGoldAccent, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    Text(
                        text = post.title,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (post.isPinned) GolfGoldAccent else Color.White
                    )
                }

                Surface(
                    color = Color(0x33000000),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = post.datePosted,
                        color = Color(0x88FFFFFF),
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "By ${post.authorName} (${post.authorRole.uppercase()})",
                fontSize = 11.sp,
                color = Color(0xFFA5D6A7),
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = post.message,
                fontSize = 13.sp,
                color = Color(0xEEFFFFFF),
                lineHeight = 18.sp
            )

            // Replies section
            if (replies.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0x55000000), RoundedCornerShape(8.dp))
                        .padding(8.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    replies.forEach { r ->
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "${r.authorName}: ",
                                color = GolfGoldAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = r.replyText,
                                color = Color.White,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Reply trigger / input
            if (!showReplyInput) {
                TextButton(
                    onClick = { showReplyInput = true },
                    modifier = Modifier.align(Alignment.End).testTag("reply_btn_${post.id}")
                ) {
                    Text("Reply (${replies.size})", color = GolfGoldAccent, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = replyText,
                        onValueChange = { replyText = it },
                        placeholder = { Text("Write a reply...", color = Color(0x66FFFFFF), fontSize = 11.sp) },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = GolfGoldAccent,
                            unfocusedBorderColor = Color(0x44FFFFFF)
                        ),
                        modifier = Modifier.weight(1f).testTag("reply_input_${post.id}")
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = {
                            if (replyText.isNotBlank()) {
                                onReply(replyText.trim())
                                replyText = ""
                                showReplyInput = false
                            }
                        },
                        modifier = Modifier.testTag("send_reply_${post.id}")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send", tint = GolfGoldAccent, modifier = Modifier.size(18.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun CreateNoticeDialog(
    onDismiss: () -> Unit,
    onPost: (title: String, msg: String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var msg by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F2615)),
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, GolfGoldAccent),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Text("New Announcement", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Post an announcement visible to all golfers and caddies", fontSize = 11.sp, color = GolfGoldAccent)
                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Announcement Title") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("new_notice_title")
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = msg,
                    onValueChange = { msg = it },
                    label = { Text("Message Body") },
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = GolfGoldAccent
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("new_notice_message")
                )

                if (error != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(text = error!!, color = Color(0xFFFF8A80), fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    TextButton(onClick = onDismiss) { Text("Cancel", color = Color(0xAAFFFFFF)) }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (title.isBlank() || msg.isBlank()) {
                                error = "Please provide both title and message"
                            } else {
                                onPost(title.trim(), msg.trim())
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = GolfGoldAccent)
                    ) {
                        Text("Post Notice", color = Color(0xFF1B241C), fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
