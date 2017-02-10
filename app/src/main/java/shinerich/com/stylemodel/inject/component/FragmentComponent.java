package shinerich.com.stylemodel.inject.component;

import android.app.Activity;

import dagger.Component;
import shinerich.com.stylemodel.inject.module.FragmentModule;
import shinerich.com.stylemodel.inject.scope.FragmentScope;
import shinerich.com.stylemodel.ui.main.fragment.CommonInformationFragment;
import shinerich.com.stylemodel.ui.main.fragment.HomePageFragment;
import shinerich.com.stylemodel.ui.subscription.fragment.MineSubFragment;
import shinerich.com.stylemodel.ui.subscription.fragment.RecommendFragment;
import shinerich.com.stylemodel.ui.subscription.fragment.HotBloggersFragment;
import shinerich.com.stylemodel.ui.subscription.fragment.SubscriptionFragment;

/**
 * Created by Administrator on 2016/9/13.
 */
@FragmentScope
@Component(dependencies = AppComponent.class, modules = FragmentModule.class)
public interface FragmentComponent {


    Activity getActivity();

    //void inject(BaseFragment baseFragment);

    void inject(HomePageFragment mHomePageFragment);

    void inject(CommonInformationFragment commonInformationFragment);

    void inject(SubscriptionFragment subscriptionFragment);

    void inject(RecommendFragment recommendFragment);

    void inject(HotBloggersFragment subscriptionContentFragment);

    void inject(MineSubFragment mineSubFragment);


}
