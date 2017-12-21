package com.ecjia.view.fragment;


import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ecjia.base.BaseFragment;
import com.ecjia.consts.IntentKeywords;
import com.ecjia.consts.SharedPKeywords;
import com.ecjia.entity.responsemodel.SESSION;
import com.ecjia.util.EventBus.MyEvent;
import com.ecjia.view.activity.user.LoginActivity;
import com.ecjia.view.fragment.homefragment.fragment.NewHomeFragment;
import com.ecmoban.android.sijiqing.R;

/**
 * 首页底部的设置
 *
 * @author Administrator
 */
public class TabsFragment extends BaseFragment {

    ImageView tab_one;
    ImageView tab_two;
    ImageView tab_three;
    ImageView tab_four;
    ImageView tab_five;


    public interface FragmentListener {
        public void addIgnoredView(View v);

        public void removeIgnoredView(View v);

        public void Open();
    }

    private FrameLayout itemone, itemtwo, itemthree, itemfour, itemfive;

    private TextView textone, texttwo, textthree, textfour, textfive;

    public static boolean select_one;

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;
    NewHomeFragment homeFragment;
    SearchFragment searchFragment;
    ShoppingCartFragment shoppingCartFragment;
    MineFragment shopListFragment;
    FoundFragment myFindFragment;
    private String tab_position;
    private LinearLayout shopping_cart_num_bg_one;
    private TextView shopping_cart_num;
    int activecolor;
    int color;

    public TabsFragment() {
        instance = this;
    }

    @Override
    public void onEvent(MyEvent event) {

    }

    private static TabsFragment instance;

    public static TabsFragment getInstance() {
        if (instance == null) {
            synchronized (TabsFragment.class) {
                if (instance == null) {
                    instance = new TabsFragment();
                }
            }
        }
        return instance;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mainView = inflater.inflate(R.layout.toolbar, container, false);
        activecolor = getActivity().getResources().getColor(R.color.public_theme_color_normal);
        color = getActivity().getResources().getColor(R.color.filter_text_color);
        init(mainView);

        shared = getActivity().getSharedPreferences(SharedPKeywords.SPUSER, 0);
        editor = shared.edit();
        return mainView;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onActivityCreated(Bundle paramBundle) {
        super.onActivityCreated(paramBundle);
        setRetainInstance(true);
    }

    void init(View mainView) {
        shopping_cart_num = (TextView) mainView.findViewById(R.id.shopping_cart_num);
        shopping_cart_num_bg_one = (LinearLayout) mainView.findViewById(R.id.shopping_cart_num_bg_one);

        this.tab_one = (ImageView) mainView.findViewById(R.id.toolbar_tabone);
        itemone = (FrameLayout) mainView.findViewById(R.id.tabitemone);
        textone = (TextView) mainView.findViewById(R.id.toolbar_textone);
        itemone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTabSelected("tab_one");
                select_one = true;
            }
        });


        this.tab_two = (ImageView) mainView.findViewById(R.id.toolbar_tabtwo);
        itemtwo = (FrameLayout) mainView.findViewById(R.id.tabitemtwo);
        texttwo = (TextView) mainView.findViewById(R.id.toolbar_texttwo);
        itemtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTabSelected("tab_two");
            }
        });


        this.tab_three = (ImageView) mainView.findViewById(R.id.toolbar_tabthree);
        itemthree = (FrameLayout) mainView.findViewById(R.id.tabitemthree);
        textthree = (TextView) mainView.findViewById(R.id.toolbar_textthree);
        itemthree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnTabSelected("tab_three");
            }
        });


        this.tab_four = (ImageView) mainView
                .findViewById(R.id.toolbar_tabfour);
        itemfour = (FrameLayout) mainView.findViewById(R.id.tabitemfour);
        textfour = (TextView) mainView.findViewById(R.id.toolbar_textfour);
        itemfour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnTabSelected("tab_four");

                if (null == SESSION.getInstance() || "".equals(SESSION.getInstance().getUid())) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    getActivity().overridePendingTransition(R.anim.push_buttom_in, R.anim.push_buttom_out);
                }
            }

        });

        this.tab_five = (ImageView) mainView
                .findViewById(R.id.toolbar_tabfive);
        itemfive = (FrameLayout) mainView.findViewById(R.id.tabitemfive);
        itemfive.setVisibility(View.GONE);
        textfive = (TextView) mainView.findViewById(R.id.toolbar_textfive);
        itemfive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                OnTabSelected("tab_five");
            }

        });


        OnTabSelected("tab_one");

    }

    public void one() {
        if (null == homeFragment) {
            homeFragment = new NewHomeFragment();
        }
        FragmentTransaction localFragmentTransaction = getFragmentManager()
                .beginTransaction();
        localFragmentTransaction.replace(R.id.fragment_container,
                homeFragment);
        localFragmentTransaction.commitAllowingStateLoss();

        tab_position = "tab_one";

        tab_one.setBackgroundResource(R.drawable.footer_home_active_icon);
        tab_two.setBackgroundResource(R.drawable.footer_search_icon);
        tab_three.setBackgroundResource(R.drawable.footer_mine_icon);
        tab_four.setBackgroundResource(R.drawable.footer_shoppingcart_icon);
        tab_five.setBackgroundResource(R.drawable.footer_find_icon);

        textone.setTextColor(activecolor);
        texttwo.setTextColor(color);
        textthree.setTextColor(color);
        textfour.setTextColor(color);
        textfive.setTextColor(color);

    }


    public void one(String category_id) {
        if (null == homeFragment) {
            homeFragment = new NewHomeFragment();
        }
        Bundle bundle = new Bundle();
        bundle.putString(IntentKeywords.CATEGORY_ID, category_id);
        homeFragment.openTypeCategoryId = category_id;
        FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
        localFragmentTransaction.replace(R.id.fragment_container, homeFragment);
        localFragmentTransaction.commitAllowingStateLoss();

        tab_position = "tab_one";

        tab_one.setBackgroundResource(R.drawable.footer_home_active_icon);
        tab_two.setBackgroundResource(R.drawable.footer_search_icon);
        tab_three.setBackgroundResource(R.drawable.footer_mine_icon);
        tab_four.setBackgroundResource(R.drawable.footer_shoppingcart_icon);
        tab_five.setBackgroundResource(R.drawable.footer_find_icon);

        textone.setTextColor(activecolor);
        texttwo.setTextColor(color);
        textthree.setTextColor(color);
        textfour.setTextColor(color);
        textfive.setTextColor(color);

    }

    public void select_three(String id) {
        if (shopListFragment == null) {
            shopListFragment = new MineFragment();
        }
//
//        Bundle bundle = new Bundle();
//        bundle.putString("category_id", id);
//        shopListFragment.setArguments(bundle);

        FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
        localFragmentTransaction.replace(R.id.fragment_container, shopListFragment);
        localFragmentTransaction.commitAllowingStateLoss();

        tab_position = "tab_three";

        this.tab_one.setBackgroundResource(R.drawable.footer_home_icon);
        this.tab_two.setBackgroundResource(R.drawable.footer_search_icon);
        this.tab_three.setBackgroundResource(R.drawable.footer_mine_icon);
        this.tab_four.setBackgroundResource(R.drawable.footer_shoppingcart_icon);
        this.tab_five.setBackgroundResource(R.drawable.footer_find_icon);

        textone.setTextColor(color);
        texttwo.setTextColor(color);
        textthree.setTextColor(activecolor);
        textfour.setTextColor(color);
        textfive.setTextColor(color);

    }


    // 切换Fragment
    public void OnTabSelected(String tabName) {

        if (tabName == "tab_one" && !tabName.equals(tab_position)) {
            tab_position = "tab_one";
            if (null == homeFragment) {
                homeFragment = new NewHomeFragment();
            }

            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, homeFragment, "tab_one");
            localFragmentTransaction.commit();

            this.tab_one.setBackgroundResource(R.drawable.footer_home_active_icon);
            this.tab_two.setBackgroundResource(R.drawable.footer_search_icon);
            this.tab_three.setBackgroundResource(R.drawable.footer_mine_icon);
            this.tab_four.setBackgroundResource(R.drawable.footer_shoppingcart_icon);
            this.tab_five.setBackgroundResource(R.drawable.footer_find_icon);

            textone.setTextColor(activecolor);
            texttwo.setTextColor(color);
            textthree.setTextColor(color);
            textfour.setTextColor(color);
            textfive.setTextColor(color);


        } else if (tabName == "tab_two" && !tabName.equals(tab_position)) {
            tab_position = "tab_two";
            if (null == searchFragment) {
                searchFragment = new SearchFragment();
            }

            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, searchFragment, "tab_two");
            localFragmentTransaction.commit();

            this.tab_one.setBackgroundResource(R.drawable.footer_home_icon);
            this.tab_two.setBackgroundResource(R.drawable.footer_search_active_icon);
            this.tab_three.setBackgroundResource(R.drawable.footer_mine_icon);
            this.tab_four.setBackgroundResource(R.drawable.footer_shoppingcart_icon);
            this.tab_five.setBackgroundResource(R.drawable.footer_find_icon);

            textone.setTextColor(color);
            texttwo.setTextColor(activecolor);
            textthree.setTextColor(color);
            textfour.setTextColor(color);
            textfive.setTextColor(color);

        } else if (tabName == "tab_three" && !tabName.equals(tab_position)) {
            tab_position = "tab_three";
            if (shopListFragment == null) {
                shopListFragment = new MineFragment();
            }
            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, shopListFragment, "tab_three");
            localFragmentTransaction.commit();

            this.tab_one.setBackgroundResource(R.drawable.footer_home_icon);
            this.tab_two.setBackgroundResource(R.drawable.footer_search_icon);
            this.tab_three.setBackgroundResource(R.drawable.footer_mine_active_icon);
            this.tab_four.setBackgroundResource(R.drawable.footer_shoppingcart_icon);
            this.tab_five.setBackgroundResource(R.drawable.footer_find_icon);

            textone.setTextColor(color);
            texttwo.setTextColor(color);
            textthree.setTextColor(activecolor);
            textfour.setTextColor(color);
            textfive.setTextColor(color);

        } else if (tabName == "tab_four" && !tabName.equals(tab_position)) {
            tab_position = "tab_four";
            shoppingCartFragment = new ShoppingCartFragment();

            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, shoppingCartFragment, "tab_four");
            localFragmentTransaction.commit();

            this.tab_one.setBackgroundResource(R.drawable.footer_home_icon);
            this.tab_two.setBackgroundResource(R.drawable.footer_search_icon);
            this.tab_three.setBackgroundResource(R.drawable.footer_mine_icon);
            this.tab_four.setBackgroundResource(R.drawable.footer_shoppingcart_active_icon);
            this.tab_five.setBackgroundResource(R.drawable.footer_find_icon);

            textone.setTextColor(color);
            texttwo.setTextColor(color);
            textthree.setTextColor(color);
            textfour.setTextColor(activecolor);
            textfive.setTextColor(color);
        } else if (tabName == "tab_five" && !tabName.equals(tab_position)) {
            tab_position = "tab_five";
            if (null == myFindFragment) {
                myFindFragment = new FoundFragment();
            }
            FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
            localFragmentTransaction.replace(R.id.fragment_container, myFindFragment, "tab_five");
            localFragmentTransaction.commit();


            this.tab_one.setBackgroundResource(R.drawable.footer_home_icon);
            this.tab_two.setBackgroundResource(R.drawable.footer_search_icon);
            this.tab_three.setBackgroundResource(R.drawable.footer_mine_icon);
            this.tab_four.setBackgroundResource(R.drawable.footer_shoppingcart_icon);
            this.tab_five.setBackgroundResource(R.drawable.footer_find_active_icon);

            textone.setTextColor(color);
            texttwo.setTextColor(color);
            textthree.setTextColor(color);
            textfour.setTextColor(color);
            textfive.setTextColor(activecolor);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // 登录成功返回到个人主页
        if (requestCode == 2) {
            if (null != data) {
                if (null == shoppingCartFragment) {
                    shoppingCartFragment = new ShoppingCartFragment();
                }

                FragmentTransaction localFragmentTransaction = getFragmentManager().beginTransaction();
                localFragmentTransaction.replace(R.id.fragment_container, shoppingCartFragment, "tab_four");
                localFragmentTransaction.commit();

                this.tab_one.setBackgroundResource(R.drawable.footer_home_icon);
                this.tab_two.setBackgroundResource(R.drawable.footer_search_icon);
                this.tab_three.setBackgroundResource(R.drawable.footer_shoplist_icon);
                this.tab_four.setBackgroundResource(R.drawable.footer_shoppingcart_active_icon);


//                this.tab_onebg.setVisibility(View.INVISIBLE);
//                this.tab_twobg.setVisibility(View.INVISIBLE);
//                this.tab_threebg.setVisibility(View.VISIBLE);
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        setShoppingcartNum();
    }

    public void setShoppingcartNum() {
        String uid = shared.getString(SharedPKeywords.SPUSER_KUID, "");
        if ("".equals(uid) || mApp.getGoodsNum() == 0) {
            shopping_cart_num_bg_one.setVisibility(View.GONE);
            shopping_cart_num.setVisibility(View.GONE);
        } else {
            shopping_cart_num_bg_one.setVisibility(View.VISIBLE);
            shopping_cart_num.setVisibility(View.VISIBLE);
            if (mApp.getGoodsNum() < 10) {
                shopping_cart_num.setText(mApp.getGoodsNum() + "");
            } else if (mApp.getGoodsNum() < 100 && mApp.getGoodsNum() > 9) {
                shopping_cart_num.setText(mApp.getGoodsNum() + "");
            } else if (mApp.getGoodsNum() > 99) {
                shopping_cart_num.setText("99+");
            }
        }
    }
}