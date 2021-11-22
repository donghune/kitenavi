package com.github.donghune.presenter.main

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.lifecycleScope
import com.github.donghune.domain.entity.Group
import com.github.donghune.presenter.group.GroupActivity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var googleMap: GoogleMap
    private val markers = mutableListOf<Marker>()
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mapView = rememberMapViewWithLifecycle()
            val groupList = viewModel.groupList.collectAsState()
            val expanded = remember { mutableStateOf(false) }
            val selectedGroup = remember { mutableStateOf<Group?>(null) }
            val groupChangeDialog = remember { mutableStateOf(false) }

            lifecycleScope.launchWhenCreated {
                viewModel.loadState.collect {
                    Log.d(TAG, "onCreate: it=[$it]")
                }
            }

            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text(text = selectedGroup.value?.name ?: "그룹을 선택해주세요") },
                        actions = {
                            DropDownMenu(
                                expanded = expanded,
                                groupChangeDialog = groupChangeDialog
                            )
                        }
                    )
                }
            ) {
                MapViewContainer(mapView = mapView)

                if (groupChangeDialog.value) {
                    GroupListAlertDialog(isVisible = groupChangeDialog, groupList = groupList) {
                        viewModel.fetchAddressListByGroup(it)
                        selectedGroup.value = it
                        groupChangeDialog.value = false
                    }
                }
            }
        }
    }

    @Composable
    fun DropDownMenu(
        expanded: MutableState<Boolean>,
        groupChangeDialog: MutableState<Boolean>,
    ) {
        Box(
            Modifier.wrapContentSize(Alignment.TopEnd)
        ) {
            IconButton(onClick = {
                expanded.value = true
            }) {
                Icon(
                    Icons.Filled.MoreVert,
                    contentDescription = "Localized description"
                )
            }

            DropdownMenu(
                expanded = expanded.value,
                onDismissRequest = { expanded.value = false },
            ) {
                DropdownMenuItem(
                    onClick = {
                        expanded.value = false
                        groupChangeDialog.value = true
                    }
                ) {
                    Text("그룹 변경")
                }

                DropdownMenuItem(
                    onClick = {
                        expanded.value = false
                        GroupActivity.startActivity(this@MainActivity)
                    }
                ) {
                    Text("그룹 관리")
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.fetchGroupList()
        lifecycleScope.launch {
            viewModel.addressList.collect { list ->
                markers.forEach { it.remove() }
                markers.clear()
                list.forEach {
                    val latLng = LatLng(it.latitude, it.longitude)
                    val marker = MarkerOptions().position(latLng).title(it.name)
                    googleMap.addMarker(marker)?.let { it1 -> markers.add(it1) }
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }
            }
        }
    }

    @Composable
    fun GroupListAlertDialog(
        isVisible: MutableState<Boolean>,
        groupList: State<List<Group>>,
        onItemClick: (Group) -> Unit
    ) {
        AlertDialog(
            onDismissRequest = {
                isVisible.value = true
            },
            text = {
                Column {
                    groupList.value.forEach {
                        GroupItem(it) {
                            onItemClick(it)
                        }
                    }
                }
            },
            confirmButton = {
            }
        )
    }

    @Composable
    fun GroupItem(group: Group, onItemClick: () -> Unit) {
        Card {
            Text(
                modifier = Modifier
                    .padding(10.dp)
                    .clickable(onClick = onItemClick),
                text = group.name
            )
        }
    }

    @Composable
    fun MapViewContainer(
        mapView: MapView
    ) {
        AndroidView(factory = { mapView }) {
            mapView.getMapAsync { map ->
                googleMap = map
            }
        }
    }

    companion object {
        private val TAG = "MainActivity"
    }
}
