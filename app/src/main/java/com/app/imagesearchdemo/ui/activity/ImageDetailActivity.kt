package com.app.imagesearchdemo.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.imagesearchdemo.R
import com.app.imagesearchdemo.ui.adapter.CommentListAdapter
import com.app.imagesearchdemo.data.ImageData
import com.app.imagesearchdemo.utils.Constant.KEY_DB_IMAGE_LIST
import com.app.imagesearchdemo.utils.Constant.KEY_IMAGE_BUNDLE
import com.app.imagesearchdemo.utils.Constant.KEY_IMAGE_DATA
import com.app.imagesearchdemo.utils.Constant.KEY_INDEX
import com.app.imagesearchdemo.utils.loadImage
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_image_detail.*


class ImageDetailActivity : AppCompatActivity() {
    private var commentList: MutableList<String> = mutableListOf()
    private var adapter: CommentListAdapter? = null
    private var index: Int? = null
    private val imageData by lazy {
        return@lazy if (intent.getBundleExtra(KEY_IMAGE_DATA)?.get(KEY_IMAGE_BUNDLE) != null) {
            intent.getBundleExtra(KEY_IMAGE_DATA)?.get(KEY_IMAGE_BUNDLE) as ImageData
        } else {
            ImageData()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_detail)
        setRecyclerView()
        setData()
        /**
         * Fetch Data From DB and display CommentList Here
         */
        index = intent.getBundleExtra(KEY_IMAGE_DATA)?.getInt(KEY_INDEX)
        val dbList: MutableList<ImageData> = Paper.book().read(KEY_DB_IMAGE_LIST)
        commentList.addAll(dbList[index!!].commentList)
        adapter?.notifyDataSetChanged()
        buttonSubmit.setOnClickListener(this::onClick)
    }

    private fun onClick(view: View) {
        when (view.id) {
            R.id.buttonSubmit -> {
                if (editTextComment.text.toString().isNotEmpty()) {
                    addComment()
                }
            }
        }
    }

    private fun addComment() {
        /**
         * Add Comment and Also Sore in DB
         */
        if (index != null) {
            val dbList: MutableList<ImageData> = Paper.book().read(KEY_DB_IMAGE_LIST)
            dbList[index!!].commentList.add(editTextComment.text.toString())
            commentList.add(editTextComment.text.toString())
            adapter?.notifyDataSetChanged()
            editTextComment.setText("")
            Paper.book().delete(KEY_DB_IMAGE_LIST)
            Paper.book().write(KEY_DB_IMAGE_LIST, dbList)
        }
    }

    private fun setData() {
        try {
            this.supportActionBar?.title = imageData.title
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            imageView.loadImage(imageData.images!![0]!!.link!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setRecyclerView() {
        adapter = CommentListAdapter(commentList) {}
        recyclerView.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(this)
        val dividerItemDecoration = DividerItemDecoration(
            recyclerView.context,
            linearLayoutManager.orientation
        )
        recyclerView.addItemDecoration(dividerItemDecoration)
        recyclerView.layoutManager = linearLayoutManager
    }
}