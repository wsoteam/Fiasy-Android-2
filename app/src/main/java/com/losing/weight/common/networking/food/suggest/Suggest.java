
package com.losing.weight.common.networking.food.suggest;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Suggest {

    @SerializedName("name_suggest__completion")
    @Expose
    private List<NameSuggestCompletion> nameSuggestCompletion = null;

    public List<NameSuggestCompletion> getNameSuggestCompletion() {
        return nameSuggestCompletion;
    }

    public void setNameSuggestCompletion(List<NameSuggestCompletion> nameSuggestCompletion) {
        this.nameSuggestCompletion = nameSuggestCompletion;
    }

    public Suggest withNameSuggestCompletion(List<NameSuggestCompletion> nameSuggestCompletion) {
        this.nameSuggestCompletion = nameSuggestCompletion;
        return this;
    }

}
