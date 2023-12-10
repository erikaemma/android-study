package com.bignerdranch.android.photogallery

import android.app.Application
import androidx.lifecycle.*

//class PhotoGalleryViewModel : ViewModel() {
class PhotoGalleryViewModel(private val app: Application) : AndroidViewModel(app) {

    val galleryItemLiveData: LiveData<List<GalleryItem>>

    private val flickrFetchr = FlickrFetchr()
    private val mutableSearchTerm = MutableLiveData<String>()

    val searchTerm: String
        get() = mutableSearchTerm.value ?: ""

    init {
        //mutableSearchTerm.value = "planets"
        mutableSearchTerm.value = QueryPreferences.getStoredQuery(app)

        //galleryItemLiveData = FlickrFetchr().fetchPhotos()
        //galleryItemLiveData = FlickrFetchr().searchPhotos("cat")
        galleryItemLiveData = Transformations.switchMap(mutableSearchTerm) {searchTerm ->
            if(searchTerm.isBlank()) {
                flickrFetchr.fetchPhotos()
            } else {
                flickrFetchr.searchPhotos(searchTerm)
            }
        }
    }

    fun fetchPhotos(query: String = "") {
        QueryPreferences.setStoredQuery(app, query)
        mutableSearchTerm.value = query
    }

}