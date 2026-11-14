package com.contentnex.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val Ink = Color(0xFF17213A)
private val Violet = Color(0xFF6655D9)
private val Mist = Color(0xFFF4F5FB)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ContentNexTheme { ContentNexApp() } }
    }
}

@Composable
private fun ContentNexTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = lightColorScheme(
            primary = Violet, onPrimary = Color.White, secondary = Color(0xFF18A999),
            background = Mist, surface = Color.White, onSurface = Ink
        ),
        content = content
    )
}

private enum class AppTab { HOME, WRITER, PLANNER }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentNexApp() {
    var tab by remember { mutableStateOf(AppTab.HOME) }
    Scaffold(
        containerColor = Mist,
        topBar = {
            TopAppBar(title = {
                Column {
                    Text("CONTENTNEX", fontWeight = FontWeight.ExtraBold)
                    Text("Your AI-Powered Content Business Studio", style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                }
            })
        },
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(tab == AppTab.HOME, { tab = AppTab.HOME }, { Icon(Icons.Default.Home, null) }, label = { Text("หน้าหลัก") })
                NavigationBarItem(tab == AppTab.WRITER, { tab = AppTab.WRITER }, { Icon(Icons.Default.EditNote, null) }, label = { Text("เขียนคอนเทนต์") })
                NavigationBarItem(tab == AppTab.PLANNER, { tab = AppTab.PLANNER }, { Icon(Icons.Default.CalendarMonth, null) }, label = { Text("วางแผน") })
            }
        }
    ) { padding ->
        when (tab) {
            AppTab.HOME -> HomeScreen(Modifier.padding(padding))
            AppTab.WRITER -> WriterScreen(Modifier.padding(padding))
            AppTab.PLANNER -> PlannerScreen(Modifier.padding(padding))
        }
    }
}

@Composable
private fun HomeScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Card(colors = CardDefaults.cardColors(containerColor = Ink), shape = RoundedCornerShape(24.dp)) {
            Column(Modifier.padding(22.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFFFFD166))
                    Spacer(Modifier.width(8.dp))
                    Text("CONTENT STUDIO", color = Color(0xFFFFD166), fontWeight = FontWeight.Bold)
                }
                Spacer(Modifier.height(12.dp))
                Text("เปลี่ยนไอเดียให้เป็นคอนเทนต์ขายของ", color = Color.White, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(8.dp))
                Text("เครื่องมือช่วยคิดและจัดทำเนื้อหาสำหรับร้านค้าออนไลน์ เริ่มใช้ได้โดยไม่ต้องเชื่อม API", color = Color(0xFFD9DDF0))
            }
        }
        Text("เครื่องมือของคุณ", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        FeatureCard("AI Copywriter", "ร่างแคปชันและข้อความโปรโมตสินค้า", "เริ่มเขียนได้จากแท็บด้านล่าง")
        FeatureCard("Content Planner", "จัดแนวคิดคอนเทนต์ให้เป็นแผนรายวัน", "รุ่นเริ่มต้น")
        FeatureCard("Poster & Video Studio", "เตรียมแนวทางภาพและสคริปต์วิดีโอ", "อยู่ในแผนพัฒนาถัดไป")
        Text("หมายเหตุ: รุ่นนี้ใช้เทมเพลตภายในแอป ยังไม่ได้เชื่อมต่อโมเดล AI ภายนอก", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
    }
}

@Composable
private fun FeatureCard(title: String, description: String, status: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
        Column(Modifier.fillMaxWidth().padding(18.dp)) {
            Text(title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(4.dp))
            Text(description, color = Color.DarkGray)
            Spacer(Modifier.height(8.dp))
            Text(status, color = Violet, style = MaterialTheme.typography.labelMedium)
        }
    }
}

@Composable
private fun WriterScreen(modifier: Modifier = Modifier) {
    var product by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var tone by remember { mutableStateOf("เป็นกันเอง") }
    var output by remember { mutableStateOf("") }
    val clipboard = LocalClipboardManager.current
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text("ผู้ช่วยเขียนคอนเทนต์", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("กรอกข้อมูลสินค้า แล้วสร้างร่างข้อความจากเทมเพลตในเครื่อง")
        OutlinedTextField(product, { product = it }, Modifier.fillMaxWidth(), label = { Text("ชื่อสินค้า") }, placeholder = { Text("เช่น เสื้อยืดโอเวอร์ไซซ์") }, singleLine = true)
        OutlinedTextField(details, { details = it }, Modifier.fillMaxWidth(), label = { Text("จุดเด่น / รายละเอียดสินค้า") }, placeholder = { Text("เช่น ผ้านุ่ม ใส่สบาย มี 3 สี") }, minLines = 3)
        OutlinedTextField(tone, { tone = it }, Modifier.fillMaxWidth(), label = { Text("โทนภาษา") }, singleLine = true)
        Button(onClick = {
            output = if (product.isBlank()) "กรุณากรอกชื่อสินค้าก่อนสร้างข้อความ"
            else buildCaption(product.trim(), details.trim(), tone.trim())
        }, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Default.AutoAwesome, null)
            Spacer(Modifier.width(8.dp))
            Text("สร้างร่างคอนเทนต์")
        }
        if (output.isNotBlank()) {
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(18.dp)) {
                Column(Modifier.padding(16.dp)) {
                    Text("ร่างข้อความของคุณ", fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(8.dp))
                    Text(output)
                    Spacer(Modifier.height(12.dp))
                    Button(onClick = { clipboard.setText(AnnotatedString(output)) }, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.ContentCopy, null)
                        Spacer(Modifier.width(8.dp))
                        Text("คัดลอกข้อความ")
                    }
                }
            }
        }
    }
}

private fun buildCaption(product: String, details: String, tone: String): String {
    val detailLine = if (details.isBlank()) "สินค้าดี ๆ ที่อยากให้คุณได้ลอง" else details
    val voice = if (tone.isBlank()) "เป็นกันเอง" else tone
    return """
        ✨ ${product} มาแล้ว!

        ${detailLine}

        เหมาะสำหรับคนที่กำลังมองหาสินค้าคุณภาพในสไตล์ของตัวเอง 💜
        สนใจสอบถามรายละเอียดหรือสั่งซื้อ ทักแชตร้านค้าได้เลยนะคะ

        #${product.replace(" ", "")} #ร้านค้าออนไลน์
        [โทนภาษา: ${voice}]

        ---
        หมายเหตุ: ข้อความนี้เป็นร่างจากเทมเพลต กรุณาตรวจสอบข้อมูลจริงก่อนโพสต์
    """.trimIndent()
}

@Composable
private fun PlannerScreen(modifier: Modifier = Modifier) {
    Column(modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("Content Planner", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text("แนวทางจัดคอนเทนต์เบื้องต้นสำหรับร้านค้าออนไลน์")
        PlannerItem("วันจันทร์", "แนะนำสินค้า", "เล่าจุดเด่นและประโยชน์ที่ลูกค้าจะได้รับ")
        PlannerItem("วันอังคาร", "ให้ความรู้", "ตอบคำถามที่ลูกค้ามักสงสัยเกี่ยวกับสินค้า")
        PlannerItem("วันพุธ", "รีวิว / หลักฐาน", "นำเสนอรีวิวจริงโดยได้รับอนุญาตจากเจ้าของรีวิว")
        PlannerItem("วันพฤหัสบดี", "เบื้องหลังร้าน", "เล่าขั้นตอนการเลือกสินค้า แพ็ก หรือจัดส่ง")
        PlannerItem("วันศุกร์", "โปรโมชัน", "แจ้งข้อเสนอที่ร้านมีอยู่จริง พร้อมเงื่อนไขชัดเจน")
        PlannerItem("วันเสาร์", "วิดีโอสั้น", "ทำคลิปสาธิตหรือวิธีใช้สินค้า")
        PlannerItem("วันอาทิตย์", "ถามตอบ", "ชวนผู้ติดตามถามคำถามและตอบอย่างตรงไปตรงมา")
    }
}

@Composable
private fun PlannerItem(day: String, title: String, description: String) {
    Card(colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(Modifier.fillMaxWidth().padding(16.dp)) {
            Text(day, color = Violet, fontWeight = FontWeight.Bold)
            Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(description, color = Color.DarkGray)
        }
    }
}
