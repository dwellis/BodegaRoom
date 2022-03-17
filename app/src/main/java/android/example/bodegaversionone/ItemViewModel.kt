package android.example.bodegaversionone

import androidx.lifecycle.*
import kotlinx.coroutines.launch


class ItemViewModel(private val repository: ItemRepository) : ViewModel() {

    // uses LiveData and caching on allItems
    // puts the observer on the data to only update when changed
    // Repository is now separated from UI through the ViewModel MVVM
    val allItems : LiveData<List<MyDataItem>> = repository.allItems.asLiveData()

    // launches coroutine to insert the data without blocking
    fun insert(item: MyDataItem) =  viewModelScope.launch {
        repository.insert(item)
    }
}

class ItemViewModelFactory(private val repository: ItemRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>) : T {
        if(modelClass.isAssignableFrom(ItemViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ItemViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class")
    }
}