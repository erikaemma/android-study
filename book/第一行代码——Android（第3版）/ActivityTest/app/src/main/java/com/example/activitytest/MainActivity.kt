package com.example.activitytest

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.activitytest.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toSecondButton1.setOnClickListener {
            // 显式Intent
            val intent = Intent(this, SecondActivity::class.java)
            intent.putExtra("extra_key", "从MainActivity传递的数据")
            startActivity(intent)
        }

        binding.toSecondButton2.setOnClickListener {
            // 隐式Intent
            val intent = Intent("com.example.activitytest.ACTION_START")
            // 如果不是DEFAULT
            intent.addCategory("com.example.activitytest.MY_CATEGORY") //可以添加多个Category
            startActivity(intent)
        }

        binding.callWebBrowser.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW) // ThirdActivity也可响应
            intent.data= Uri.parse("http://www.baidu.com/")
            startActivity(intent)
        }

        binding.callCall.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data= Uri.parse("tel:10086")
            startActivity(intent)
        }

        binding.toFourthButton.setOnClickListener {
            val intent = Intent(this, FourthActivity::class.java)
            startActivityForResult(intent, 1) // 会回调下面的onActivityResult()方法
        }

        binding.exitButton.setOnClickListener {
            finish() // 销毁Activity
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.test_menu, menu) //getMenuInflater()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) { //getItemId()
            R.id.menu_test1 -> Toast.makeText(this, "测试菜单1", Toast.LENGTH_SHORT).show()
            R.id.menu_test2 -> Toast.makeText(this, "测试菜单2", Toast.LENGTH_SHORT).show()
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode) {
            1 -> { // 对应startActivityForResult()第2个参数
                if(resultCode == RESULT_OK) {
                    val returnedData = data?.getStringExtra("return_extra")
                    Toast.makeText(this, "接收到的返回数据：$returnedData", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
