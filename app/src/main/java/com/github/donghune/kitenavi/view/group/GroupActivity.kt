package com.github.donghune.kitenavi.view.group

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.github.donghune.kitenavi.R
import com.github.donghune.kitenavi.model.local.Group
import com.github.donghune.kitenavi.view.address.AddressActivity
import com.github.donghune.kitenavi.view.component.DoubleLineCard
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GroupActivity : ComponentActivity() {

    private val viewModel by lazy { GroupViewModel.Factory(applicationContext).build() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val openDialog = remember { mutableStateOf(false) }
            val groupList = remember { mutableStateOf(listOf<Group>()) }

            lifecycleScope.launch {
                repeatOnLifecycle(Lifecycle.State.STARTED) {
                    viewModel.groupList.collect { groupList.value = it }
                }
            }

            viewModel.fetchGroupList()

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = "그룹 관리") }
                    )
                },
                floatingActionButton = {
                    FloatingActionButton(
                        onClick = {
                            openDialog.value = true
                        }
                    ) {
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.ic_baseline_add_circle_24),
                            contentDescription = ""
                        )
                    }
                }
            ) {
                Column(
                    modifier = Modifier.verticalScroll(ScrollState(0))
                ) {
                    groupList.value.forEach {
                        GroupCard(group = it) {
                            viewModel.removeGroup(it)
                            viewModel.fetchGroupList()
                            Toast.makeText(
                                this@GroupActivity,
                                "그룹을 삭제하였습니다.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
                if (openDialog.value) {
                    GroupInsertDialog(openDialog)
                }
            }
        }
    }

    @Composable
    fun GroupCard(group: Group, deleteButtonClick: () -> Unit) {
        DoubleLineCard(
            title = group.id.toString(),
            text = group.name,
            onClick = {
                AddressActivity.startActivity(this@GroupActivity, group.id)
            },
            buttonName = "삭제",
            buttonClick = deleteButtonClick
        )
    }

    @Composable
    fun GroupInsertDialog(
        dialogVisible: MutableState<Boolean>
    ) {
        val input = remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = {
                dialogVisible.value = false
            },
            title = {
                Text(text = "그룹이름을 입력해주세요")
            },
            text = {
                TextField(
                    value = input.value,
                    onValueChange = { value -> input.value = value }
                )
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.insertGroup(input.value)
                    viewModel.fetchGroupList()
                    dialogVisible.value = false
                }) {
                    Text(text = "확인")
                }
            }
        )
    }

    companion object {
        fun startActivity(context: Context) {
            context.startActivity(Intent(context, GroupActivity::class.java))
        }
    }
}
