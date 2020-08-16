package com.app.imagesearchdemo.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.app.imagesearchdemo.R
import com.app.imagesearchdemo.ui.adapter.ImageListAdapter
import com.app.imagesearchdemo.data.ImageData
import com.app.imagesearchdemo.utils.Constant.KEY_DB_IMAGE_LIST
import com.app.imagesearchdemo.utils.Constant.KEY_IMAGE_BUNDLE
import com.app.imagesearchdemo.utils.Constant.KEY_IMAGE_DATA
import com.app.imagesearchdemo.utils.Constant.KEY_INDEX
import com.app.imagesearchdemo.utils.Constant.KEY_VANILLA
import com.app.imagesearchdemo.utils.EditTextDebounce
import com.app.imagesearchdemo.utils.showToast
import com.app.imagesearchdemo.utils.viewModelProvider
import com.app.imagesearchdemo.viewModel.ImageViewModel
import dagger.android.support.DaggerAppCompatActivity
import io.paperdb.Paper
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class ImageListActivity : DaggerAppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var imageViewModel: ImageViewModel
    private var imageList: MutableList<ImageData> = mutableListOf()
    private var dbList: MutableList<ImageData> = mutableListOf()
    private var editTextDebounce: EditTextDebounce? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.supportActionBar?.title =getString(R.string.label_image_search)
        Paper.init(this)
        getImageData()
        /**
         * if Record found in DB it's store in dbList
         */
        dbList = Paper.book().read(KEY_DB_IMAGE_LIST, mutableListOf())
        /**
         * setSearch with Debounce
         */
        setSearch()
    }

    private fun setSearch() {
        editTextDebounce = EditTextDebounce(editTextSearch)
        editTextDebounce?.watch { it ->
            imageViewModel.getImageList(
                if (it.isNotEmpty()) {
                    it
                } else {
                    KEY_VANILLA
                }
            )
        }
    }

    private fun setRecyclerView() {
        val adapter = ImageListAdapter(imageList) { index, imageData ->
            startActivity(
                Intent(this, ImageDetailActivity::class.java)
                    .putExtra(
                        KEY_IMAGE_DATA,
                        bundleOf(KEY_IMAGE_BUNDLE to imageData, KEY_INDEX to index)
                    )
            )
        }
        recyclerView.adapter = adapter
        val linearLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = linearLayoutManager
    }

    private fun getImageData() {
        imageViewModel = viewModelProvider(viewModelFactory)
        imageViewModel.getImageList(KEY_VANILLA)
        imageViewModel.imageDataSuccess.observe(this, Observer {
            imageList.clear()
            /**
             * Check Status and Success of API
             */
            if (it.success && it.status == 200) {
                if (it.data!!.isEmpty()) {
                    showToast(getString(R.string.error_msg_no_data_found))
                } else {
                    imageList.addAll(it.data)
                    /**
                     * Here Add Comment if Previous in DB
                     */
                    if (dbList.isNotEmpty()) {
                        for (i in 0 until imageList.size) {
                            for (j in 0 until dbList.size - 1) {
                                if (imageList[i].id == dbList[j].id) {
                                    imageList[i].commentList.addAll(dbList[j].commentList)
                                }
                            }
                        }
                    }
                    /**
                     * Here Delete KEY_DB_IMAGE_LIST record from DB And Then Add Data to DB Because of duplicate
                     */
                    Paper.book().delete(KEY_DB_IMAGE_LIST)
                    Paper.book().write(KEY_DB_IMAGE_LIST, imageList)
                }
            }
            setRecyclerView()
        })
        imageViewModel.imageDataFailure.observe(this, Observer {
            showToast(it)
        })
    }
}