package com.example.quickidenti.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.quickidenti.api.People
import com.example.quickidenti.app.people
import com.example.quickidenti.app.retrofit
import com.example.quickidenti.app.user
import com.example.quickidenti.dto.People.PeopleList
import com.example.quickidenti.navigation.QuickIdentiAppRouter
import com.example.quickidenti.navigation.Screen
import com.example.quickidenti.ui.theme.Primary
import com.example.quickidenti.ui.theme.Secondary
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep

@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PeopleListScreen(){
    var peoples: MutableList<PeopleList> = mutableListOf(PeopleList(0,"dummy"))
    val listApi = retrofit.create(People::class.java)
    CoroutineScope(Dispatchers.IO).launch {
        peoples = listApi.getPeoples(user.value)

    }
    sleep(100)
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(28.dp)
    ){
        val elements: MutableList<Element> =  mutableListOf(Element(0,"dummy"))
        elements.removeAt(0)
        for (i in 0..<peoples.size){
            elements.add(Element(i, peoples[i].fullname))
        }
        Column{
            SimpleLazyColumnScreen(peoples)
            Box(modifier = Modifier
                .padding(5.dp)
                .fillMaxSize(),
                contentAlignment = Alignment.BottomEnd){

                FloatingActionButton(modifier = Modifier
                    .widthIn(48.dp)
                    .heightIn(48.dp),
                    shape = RoundedCornerShape(50.dp),
                    onClick = { /*TODO*/ }) {
                    Box(modifier = Modifier
                        .widthIn(48.dp)
                        .heightIn(48.dp)
                        .background(
                            brush = Brush.radialGradient(listOf(Secondary, Primary)),
                            shape = RoundedCornerShape(50.dp)
                        ),
                        contentAlignment = Alignment.Center
                    ){
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Add worker")
                    }
            }
            }
        }
    }
    BackHandler(enabled = true) {
        QuickIdentiAppRouter.navigateTo(QuickIdentiAppRouter.historyScreenList[QuickIdentiAppRouter.lastHistoryIndex.value], false)
    }
}
@Composable
fun SimpleLazyColumnScreen(element: MutableList<PeopleList>) {
    val elements by remember { mutableStateOf(element) }
    Box {
        LazyColumn {
            items(elements) { element ->
                PersonView(id = element.id,
                    title = element.fullname,
                    deleteClick = {id ->
                    Log.i("Delete", "Man delete $id")
                },
                    onItemClick = { id ->
                        Log.i("Click", "Man click $id")
                        people.intValue = id
                        QuickIdentiAppRouter.navigateTo(Screen.PersonInfoScreen, true)
                })
            }
        }
    }
}
@Composable
fun PersonView(id: Int,
               title: String,
               deleteClick: (Int) -> Unit,
               onItemClick: (Int) -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
                .clickable {
                    onItemClick(id)
                }
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person",
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
            )
            Text(
                text = title,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Image(
                imageVector = Icons.Default.Delete,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterVertically)
                    .clickable {
                        deleteClick(id)
                    }
            )

        }
    }
}
@Preview
@Composable
fun PeopleListScreenPreview(){
    PeopleListScreen()
}

class Element(var id: Int, var fullname: String)