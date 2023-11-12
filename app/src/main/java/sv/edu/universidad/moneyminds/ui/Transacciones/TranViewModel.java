package sv.edu.universidad.moneyminds.ui.Transacciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class TranViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public TranViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("$0.00");
    }

    public LiveData<String> getText() {
        return mText;
    }
}