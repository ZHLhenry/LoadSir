package com.kingja.loadsir;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;

import com.kingja.loadsir.callback.DefaultCallback;
import com.kingja.loadsir.callback.LoadCallback;

/**
 * Description:TODO
 * Create Time:2017/9/2 16:36
 * Author:KingJA
 * Email:kingjavip@gmail.com
 */
public class LoadSir {
    private LoadCallback.OnReloadListener onReloadListener;
    private LoadLayout loadLayout;
    private Builder builder = new Builder();


    public LoadSir(Builder builder) {

    }

    private LoadSir(Object target, LoadCallback.OnReloadListener onReloadListener) {
        ViewGroup contentParent = null;
        Context context;
        if (target instanceof Activity) {
            Activity activity = (Activity) target;
            context = activity;
            contentParent = (ViewGroup) activity.findViewById(android.R.id.content);
        } else if (target instanceof Fragment) {
            Fragment fragment = (Fragment) target;
            context = fragment.getActivity();
            contentParent = (ViewGroup) (fragment.getView().getParent());
        } else if (target instanceof View) {
            View view = (View) target;
            contentParent = (ViewGroup) (view.getParent());
            context = view.getContext();
        } else {
            throw new IllegalArgumentException("the argument's type must be Fragment or Activity: init(context)");
        }
        int childCount = contentParent.getChildCount();
        //get contentParent
        int index = 0;
        View oldContent;
        if (target instanceof View) {
            oldContent = (View) target;
            for (int i = 0; i < childCount; i++) {
                if (contentParent.getChildAt(i) == oldContent) {
                    index = i;
                    break;
                }
            }
        } else {
            oldContent = contentParent.getChildAt(0);
        }
        contentParent.removeView(oldContent);
        //setup content layout
        loadLayout = new LoadLayout(context,onReloadListener);

        ViewGroup.LayoutParams lp = oldContent.getLayoutParams();
        contentParent.addView(loadLayout, index, lp);
        loadLayout.addLoadCallback(DefaultCallback.createContentCallback(oldContent, context,onReloadListener));
    }

    public static LoadSir callLoadSir(Object target, LoadCallback.OnReloadListener onReloadListener) {
        return new LoadSir(target, onReloadListener);
    }

    public void showLoadCallback(int status) {
        loadLayout.showStatus(status);
    }



    public static class Builder {

        public LoadSir build() {
            return new LoadSir(this);
        }

    }

}