package shinerich.com.stylemodel.inject.component;

import android.app.Activity;

import dagger.Component;
import shinerich.com.stylemodel.inject.module.ActivityModuel;
import shinerich.com.stylemodel.inject.scope.PerActivity;
import shinerich.com.stylemodel.presenter.MainPresenter;
import shinerich.com.stylemodel.ui.main.activity.ArticleContentActivity;
import shinerich.com.stylemodel.ui.main.activity.BloggerInfoActivity;
import shinerich.com.stylemodel.ui.main.activity.ColumnActivity;
import shinerich.com.stylemodel.ui.main.activity.CommentActivity;
import shinerich.com.stylemodel.ui.main.activity.ImageListActivity;
import shinerich.com.stylemodel.ui.main.activity.MainAcitivity;
import shinerich.com.stylemodel.ui.subscription.activity.MoreBloggerActivity;
import shinerich.com.stylemodel.ui.subscription.activity.MoreContentActivity;

/**
 * Created by Administrator on 2016/8/30.
 */
@PerActivity
@Component(dependencies = AppComponent.class, modules = ActivityModuel.class)
public interface ActivityComponent {


    Activity mActivity();

    // void inject(SplashActivity splashActivity);
    MainPresenter mainPresenter();

    void inject(MainAcitivity mainAcitivity);

    void inject(ColumnActivity columnActivity);

    void inject(MoreBloggerActivity moreBloggerActivity);

    void inject(CommentActivity commentActivity);

    void inject(ArticleContentActivity articleContentActivity);

    //void inject(CommentDetailActivity commentDetailActivity);

    void inject(ImageListActivity imageListActivity);

    void inject(MoreContentActivity moreContentActivity);

    void inject(BloggerInfoActivity mBloggerInfoAcitivity);


}
