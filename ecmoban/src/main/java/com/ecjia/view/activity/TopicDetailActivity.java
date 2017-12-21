package com.ecjia.view.activity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ecjia.base.BaseActivity;
import com.ecmoban.android.sijiqing.R;
import com.ecjia.consts.ProtocolConst;
import com.ecjia.network.netmodle.TopicModel;
import com.ecjia.widgets.MyListView;
import com.ecjia.widgets.XListView;
import com.ecjia.view.adapter.TopicDetailPopAdapter;
import com.ecjia.view.adapter.TopicInfoAdapter;
import com.ecjia.network.HttpResponse;
import com.ecjia.entity.responsemodel.PAGINATED;
import com.ecjia.entity.responsemodel.STATUS;
import com.ecjia.util.ImageLoaderForLV;
import com.ecjia.util.LG;

import org.json.JSONException;
import org.json.JSONObject;

public class TopicDetailActivity extends BaseActivity implements HttpResponse,XListView.IXListViewListener{

    private ImageView back;
    private TextView title;
    private LinearLayout topic_top_category;
    private String type="";
    private String sort_by="hot";
    private String topic_id;
    private XListView listView;
    private FrameLayout null_page;
    private View headview,headview2;

    private LinearLayout tab_big_item;
    private RelativeLayout tab1,tab2,tab3,tab4,head_tab1,head_tab2,head_tab3,head_tab4;
    private TextView tab_text1,tab_text2,tab_text3,tab_text4,head_tab_text1,head_tab_text2,head_tab_text3,head_tab_text4;
    private View tab_line1,tab_line2,tab_line3,tab_line4,head_tab_line1,head_tab_line2,head_tab_line3,head_tab_line4;

    private int selected_color,unselected_color;

    private TopicModel topicModel;
    private TopicInfoAdapter topicinfoAdapter;

    private PopupWindow popupWindow;
    private View popupWindow_buttom_view;
    private MyListView poplistview;
    private TopicDetailPopAdapter topicDetailPopAdapter;
    private LinearLayout detail_view;
    private FrameLayout topic_top_item;
    private ImageView top_img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
        initView();
    }

    private void initView() {
        topic_id=getIntent().getStringExtra("topic_id");
        topicModel=new TopicModel(this);
        topicModel.addResponseListener(this);
        selected_color=res.getColor(R.color.public_theme_color_normal);
        unselected_color=res.getColor(R.color.my_black);
        initHeadView();
        initHeadView2();
        back= (ImageView) findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        detail_view= (LinearLayout) findViewById(R.id.topic_detail_content);
        topic_top_item= (FrameLayout) findViewById(R.id.topic_top_item);
        title= (TextView) findViewById(R.id.title_text);
        topic_top_category= (LinearLayout) findViewById(R.id.topic_top_category);
        topic_top_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(topicModel.topic.getTopic_types().size()>0){
                    popupWindow.showAsDropDown(topic_top_item);
                    topic_top_category.setBackgroundResource(R.drawable.circle_close_button);
                }
            }
        });
        topic_top_category.setClickable(false);
        listView= (XListView) findViewById(R.id.topic_detail_list);
        listView.addHeaderView(headview);
        listView.addHeaderView(headview2);
        listView.setXListViewListener(this,1);
        tab_big_item= (LinearLayout) findViewById(R.id.tab_big_item);
        tab_big_item.setVisibility(View.GONE);
        listView.setPullLoadEnable(false);
        null_page= (FrameLayout) findViewById(R.id.null_pager);
        topicinfoAdapter=new TopicInfoAdapter(this,topicModel.topic_info_list);
        listView.setAdapter(topicinfoAdapter);


        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int i2, int i3) {
                if (firstVisibleItem > 1) {
                    tab_big_item.setVisibility(View.VISIBLE);
                } else {

                    tab_big_item.setVisibility(View.GONE);
                }
            }
        });

        tab1= (RelativeLayout) findViewById(R.id.tab_item_one);
        tab2= (RelativeLayout) findViewById(R.id.tab_item_two);
        tab3= (RelativeLayout) findViewById(R.id.tab_item_three);
        tab4= (RelativeLayout) findViewById(R.id.tab_item_four);
        tab_text1= (TextView) findViewById(R.id.tab_item_one_text);
        tab_text2= (TextView) findViewById(R.id.tab_item_two_text);
        tab_text3= (TextView) findViewById(R.id.tab_item_three_text);
        tab_text4= (TextView) findViewById(R.id.tab_item_four_text);
        tab_line1=findViewById(R.id.tab_item_one_line);
        tab_line2=findViewById(R.id.tab_item_two_line);
        tab_line3=findViewById(R.id.tab_item_three_line);
        tab_line4=findViewById(R.id.tab_item_four_line);
        tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab("one");
            }
        });
        tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab("two");
            }
        });
        tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab("three");
            }
        });
        tab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab("four");
            }
        });

        selectTab("one");
    }

    //初始化popuwindow
    private void initPopWindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.topicdetail_popupwindow, null, true);
        popupWindow_buttom_view=view.findViewById(R.id.topic_buttom_view);

        poplistview = (MyListView) view.findViewById(R.id.topic_pop_list);
        if(null!=topicModel.topic.getTopic_types()&&topicModel.topic.getTopic_types().size()>0) {
            topic_top_category.setClickable(true);
            topic_top_category.setVisibility(View.VISIBLE);
            topicDetailPopAdapter = new TopicDetailPopAdapter(this, topicModel.topic.getTopic_types());
            topicDetailPopAdapter.setOnRightItemClickListener(new TopicDetailPopAdapter.onRightItemClickListener() {
                @Override
                public void onRightItemClick(View v, int position) {
                    if(position==0){
                        type="";
                    }else {
                        type = topicDetailPopAdapter.types.get(position).getType_text();
                    }
                    LG.i("i="+position);
                    topicDetailPopAdapter.setSelectedPosition(position);
                    topicModel.getTopicInfo(topic_id,type,sort_by);
                    popupWindow.dismiss();
                }
            });
            poplistview.setAdapter(topicDetailPopAdapter);
        }else{
            topic_top_category.setVisibility(View.GONE);
        }

        //设置popupWindow的大小
        popupWindow = new PopupWindow(detail_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        //设置popupWindow的布局容器0
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);// 这里设置显示PopuWindow之后在外面点击是否有效。如果为false的话，那么点击PopuWindow外面并不会关闭PopuWindow。当然这里很明显只能在Touchable下才能使用。
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0x00999999));
        popupWindow.setAnimationStyle(R.style.alpha_anim_style);
        popupWindow_buttom_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                topic_top_category.setBackgroundResource(R.drawable.top_category);
            }
        });

    }

    private void selectTab(String selected) {
        if("one".equals(selected)){
            tab_text1.setTextColor(selected_color);
            tab_text2.setTextColor(unselected_color);
            tab_text3.setTextColor(unselected_color);
            tab_text4.setTextColor(unselected_color);

            tab_line1.setVisibility(View.VISIBLE);
            tab_line2.setVisibility(View.GONE);
            tab_line3.setVisibility(View.GONE);
            tab_line4.setVisibility(View.GONE);

            head_tab_text1.setTextColor(selected_color);
            head_tab_text2.setTextColor(unselected_color);
            head_tab_text3.setTextColor(unselected_color);
            head_tab_text4.setTextColor(unselected_color);

            head_tab_line1.setVisibility(View.VISIBLE);
            head_tab_line2.setVisibility(View.GONE);
            head_tab_line3.setVisibility(View.GONE);
            head_tab_line4.setVisibility(View.GONE);

            sort_by="new";
        }else if("two".equals(selected)){
            tab_text1.setTextColor(unselected_color);
            tab_text2.setTextColor(selected_color);
            tab_text3.setTextColor(unselected_color);
            tab_text4.setTextColor(unselected_color);

            tab_line1.setVisibility(View.GONE);
            tab_line2.setVisibility(View.VISIBLE);
            tab_line3.setVisibility(View.GONE);
            tab_line4.setVisibility(View.GONE);

            head_tab_text1.setTextColor(unselected_color);
            head_tab_text2.setTextColor(selected_color);
            head_tab_text3.setTextColor(unselected_color);
            head_tab_text4.setTextColor(unselected_color);

            head_tab_line1.setVisibility(View.GONE);
            head_tab_line2.setVisibility(View.VISIBLE);
            head_tab_line3.setVisibility(View.GONE);
            head_tab_line4.setVisibility(View.GONE);

            sort_by="hot";
        }else if("three".equals(selected)){
            tab_text1.setTextColor(unselected_color);
            tab_text2.setTextColor(unselected_color);
            tab_text3.setTextColor(selected_color);
            tab_text4.setTextColor(unselected_color);

            tab_line1.setVisibility(View.GONE);
            tab_line2.setVisibility(View.GONE);
            tab_line3.setVisibility(View.VISIBLE);
            tab_line4.setVisibility(View.GONE);

            head_tab_text1.setTextColor(unselected_color);
            head_tab_text2.setTextColor(unselected_color);
            head_tab_text3.setTextColor(selected_color);
            head_tab_text4.setTextColor(unselected_color);

            head_tab_line1.setVisibility(View.GONE);
            head_tab_line2.setVisibility(View.GONE);
            head_tab_line3.setVisibility(View.VISIBLE);
            head_tab_line4.setVisibility(View.GONE);

            sort_by="price_desc";
        }else if("four".equals(selected)){
            tab_text1.setTextColor(unselected_color);
            tab_text2.setTextColor(unselected_color);
            tab_text3.setTextColor(unselected_color);
            tab_text4.setTextColor(selected_color);

            tab_line1.setVisibility(View.GONE);
            tab_line2.setVisibility(View.GONE);
            tab_line3.setVisibility(View.GONE);
            tab_line4.setVisibility(View.VISIBLE);

            head_tab_text1.setTextColor(unselected_color);
            head_tab_text2.setTextColor(unselected_color);
            head_tab_text3.setTextColor(unselected_color);
            head_tab_text4.setTextColor(selected_color);

            head_tab_line1.setVisibility(View.GONE);
            head_tab_line2.setVisibility(View.GONE);
            head_tab_line3.setVisibility(View.GONE);
            head_tab_line4.setVisibility(View.VISIBLE);

            sort_by="price_asc";
        }else{
            tab_text1.setTextColor(selected_color);
            tab_text2.setTextColor(unselected_color);
            tab_text3.setTextColor(unselected_color);
            tab_text4.setTextColor(unselected_color);

            tab_line1.setVisibility(View.VISIBLE);
            tab_line2.setVisibility(View.GONE);
            tab_line3.setVisibility(View.GONE);
            tab_line4.setVisibility(View.GONE);

            head_tab_text1.setTextColor(selected_color);
            head_tab_text2.setTextColor(unselected_color);
            head_tab_text3.setTextColor(unselected_color);
            head_tab_text4.setTextColor(unselected_color);

            head_tab_line1.setVisibility(View.VISIBLE);
            head_tab_line2.setVisibility(View.GONE);
            head_tab_line3.setVisibility(View.GONE);
            head_tab_line4.setVisibility(View.GONE);

            sort_by="hot";
        }

        topicModel.getTopicInfo(topic_id,type,sort_by);
    }

    private void initHeadView(){
        headview= LayoutInflater.from(this).inflate(R.layout.topic_head_view,null);
        top_img= (ImageView) headview.findViewById(R.id.top_img);
        LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        params.width=getDisplayMetricsWidth();
        params.height=params.width*2/5;
        top_img.setLayoutParams(params);
    }
    private void initHeadView2() {
        headview2= LayoutInflater.from(this).inflate(R.layout.topic_head_view2,null);
        head_tab1= (RelativeLayout) headview2.findViewById(R.id.head_tab_item_one);
        head_tab2= (RelativeLayout) headview2.findViewById(R.id.head_tab_item_two);
        head_tab3= (RelativeLayout) headview2.findViewById(R.id.head_tab_item_three);
        head_tab4= (RelativeLayout) headview2.findViewById(R.id.head_tab_item_four);
        head_tab_text1= (TextView) headview2.findViewById(R.id.head_tab_item_one_text);
        head_tab_text2= (TextView) headview2.findViewById(R.id.head_tab_item_two_text);
        head_tab_text3= (TextView) headview2.findViewById(R.id.head_tab_item_three_text);
        head_tab_text4= (TextView) headview2.findViewById(R.id.head_tab_item_four_text);
        head_tab_line1=headview2.findViewById(R.id.head_tab_item_one_line);
        head_tab_line2=headview2.findViewById(R.id.head_tab_item_two_line);
        head_tab_line3=headview2.findViewById(R.id.head_tab_item_three_line);
        head_tab_line4=headview2.findViewById(R.id.head_tab_item_four_line);

        head_tab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab("one");
            }
        });
        head_tab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab("two");
            }
        });
        head_tab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab("three");
            }
        });
        head_tab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectTab("four");
            }
        });
    }
    @Override
    public void onMessageResponse(String url, JSONObject jo, STATUS status) throws JSONException {
        if(url== ProtocolConst.TOPIC_INFO){
            if(status.getSucceed()==1){
                if("".equals(title.getText().toString())){
                    title.setText(topicModel.topic.getTopic_title());
                    ImageLoaderForLV.getInstance(this).setImageRes(top_img, topicModel.topic.getTopic_image());
                    initPopWindow();
                }
                topicinfoAdapter.notifyDataSetChanged();
                listView.stopRefresh();
                listView.stopLoadMore();
                listView.stopRefresh();
                PAGINATED paginated = topicModel.paginated;
                if (paginated.getMore() == 0) {
                    listView.setPullLoadEnable(false);
                } else {
                    listView.setPullLoadEnable(true);
                }
                if(topicModel.topic_info_list.size()>0){
                    listView.setVisibility(View.VISIBLE);
                    null_page.setVisibility(View.GONE);
                }else{
                    listView.setVisibility(View.GONE);
                    null_page.setVisibility(View.VISIBLE);
                }
            }else{
                listView.setVisibility(View.GONE);
                null_page.setVisibility(View.VISIBLE);
            }
        }

    }
    // 获取屏幕宽度
    public int getDisplayMetricsWidth() {
        int i = getWindowManager().getDefaultDisplay().getWidth();
        int j = getWindowManager().getDefaultDisplay().getHeight();
        return Math.min(i, j);
    }

    @Override
    public void onRefresh(int id) {
        topicModel.getTopicInfo(topic_id,type,sort_by);
    }

    @Override
    public void onLoadMore(int id) {
        topicModel.getTopicInfoMore(topic_id,type,sort_by);
    }
}
