package io.egg.jiantu.util;

public class GAConstant {
	
	//编辑类
	public static String category_edit = "editing";

	public static String pick_font_action = "pick_font";

	public static String pick_color_action = "pick_color";
	public static String pick_texture_action = "pick_texture";

    public static String remove_picture_action = "remove_picture";
    public static String remove_picture_label_library = "library";
    public static String remove_picture_label_camera = "camera";

    public static String blur_image_action = "blur_image";

    public static String darken_image_action = "darken_image";

    public static String position_text_action = "position_text";

	public static String edit_text_init_action = "edit_text_0_init";
	public static String edit_text_finish_action = "edit_text_1_finish";
	
	
	// 选取图片

	public static String category_pick_picture = "pick_picture";

//  public static String pick_picture_action = "pick_picture";
//	public static String pick_picture_lable_init = "pick_picture_0_init";
//	public static String pick_picture_label_cancel = "pick_picture_2_cancel";

	public static String pick_picture_from_library_action_init = "pick_picture_from_library_0_init";
	public static String pick_picture_from_library_action_finish = "pick_picture_from_library_1_finish";
	public static String pick_picture_from_library_action_cancel = "pick_picture_from_library_2_cancel";
	public static String pick_picture_from_library_action_fail = "pick_picture_from_library_3_fail";

	public static String pick_picture_from_taking_picture_action_init = "pick_picture_from_taking_picture_0_init";
	public static String pick_picture_from_taking_picture_action_finish = "pick_picture_from_taking_picture_1_finish";
	public static String pick_picture_from_taking_picture_action_cancel = "pick_picture_from_taking_picture_2_cancel";
	public static String pick_picture_from_taking_picture_action_fail = "pick_picture_from_taking_picture_3_fail";

    // camera

    public static String category_camera = "camera";

    public static String switch_to_camera_action = "switch_to_camera";
    public static String switch_to_camera_label_rear = "rear";
    public static String switch_to_camera_label_front = "front";

    public static String focus_camera_action = "focus_camera";
    public static String focus_camera_label_rear = "rear";
    public static String focus_camera_label_front = "front";

	//分享

    public static String category_share = "share";

	public static String share_init = "share_0_init";
	public static String share_cancel = "share_2_cancel";

	public static String share_to_wechat_session_init = "share_to_wechat_session_0_init";
	public static String share_to_wechat_session_finish = "share_to_wechat_session_1_finish";
	public static String share_to_wechat_session_cancel = "share_to_wechat_session_2_cancel";
	public static String share_to_wechat_session_fail = "share_to_wechat_session_3_fail";
	public static String share_to_wechat_timeline_init = "share_to_wechat_timeline_0_init";
	public static String share_to_wechat_timeline_finish = "share_to_wechat_timeline_1_finish";
	public static String share_to_wechat_timeline_cancel = "share_to_wechat_timeline_2_cancel";
	public static String share_to_wechat_timeline_fail = "share_to_wechat_session_3_fail";

	public static String share_to_weibo_init = "share_to_weibo_0_init";
	public static String share_to_weibo_finish = "share_to_weibo_1_finish";
	public static String share_to_weibo_cancel = "share_to_weibo_2_cancel";
	public static String share_to_weibo_fail = "share_to_weibo_3_fail";

    public static String save_to_camera_roll_init = "save_to_camera_roll_0_init";
	public static String save_to_camera_roll_finish = "save_to_camera_roll_1_finish";
	public static String save_to_camera_roll_fail = "save_to_camera_roll_3_fail";
	
	//属性类统计,保存时总是发送的事件

	public static String category_jiantu_properties = "jiantu_properties";

	public static String font_usage_action = "font_usage";

	public static String text_count_action = "text_count";

    public static String picture_source_action = "picture_source";

	public static String with_image_label = "with_image";
	public static String without_image_label = "without_image_label";

	//配图时发送的事件,如果是配图的简图,需要发送这些事件

	public static String image_usage_action = "image_usage";
	public static String with_text_label = "with_text";
	public static String without_text_label = "without_text";
	public static String blur_usage_action = "blur_usage";
	public static String darken_usage_action = "darken_usage";

	//当保存时，如果是纯色加纹理的简图,需要发送事件

    public static String color_and_texture_usage_action = "color_and_texture_usage";
}
