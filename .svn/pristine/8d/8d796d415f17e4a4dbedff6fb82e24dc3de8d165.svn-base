package com.takebox.wedding;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.nhn.android.maps.NMapActivity;
import com.nhn.android.maps.NMapController;
import com.nhn.android.maps.NMapView;
import com.nhn.android.maps.maplib.NGeoPoint;
import com.nhn.android.maps.nmapmodel.NMapError;
import com.nhn.android.maps.nmapmodel.NMapPlacemark;
import com.nhn.android.maps.overlay.NMapPOIdata;
import com.nhn.android.maps.overlay.NMapPOIitem;
import com.nhn.android.mapviewer.overlay.NMapOverlayManager;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay.OnFloatingItemChangeListener;
import com.nhn.android.mapviewer.overlay.NMapPOIdataOverlay.OnStateChangeListener;

public class NaverMapActivity extends NMapActivity {
	private static final String LOG_TAG = "LOG_TAG";
	private static final boolean DEBUG = false;
	public static final int RESULTCODE = 1;
	
	NMapView mMapView;
	NMapController mMapController;
	public String APP_KEY = "2b4440bae28b9e0f5b20e24967f72f95";
	NMapViewerResourceProvider mMapViewerResourceProvider;
	NMapOverlayManager mOverlayManager;
	
	private NMapPOIitem mFloatingPOIitem;
	private NMapPOIdataOverlay mFloatingPOIdataOverlay;
	private Bitmap map;
	
	public String latitude;
	public String longitude;
	public String flag;
	public String place;
	double lati = 37.5666091;
	double longi = 126.978371;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // set data provider listener
	    super.setMapDataProviderListener(onDataProviderListener);
	    
	    Intent intent = getIntent();
	    latitude = intent.getStringExtra("latitude");
	    longitude = intent.getStringExtra("longitude");
	    flag = intent.getStringExtra("flag");
	    place = intent.getStringExtra("place");
	    
	    if(latitude != null && longitude != null){
	    	lati = Double.parseDouble(latitude);
	    	longi = Double.parseDouble(longitude);
	    }
	
	    // create map view
	    mMapView = new NMapView(this);

	    // set a registered API key for Open MapViewer Library
	    mMapView.setApiKey(APP_KEY);

	    // set the activity content to the map view
	    setContentView(mMapView);

	    // initialize map view
	    mMapView.setClickable(true);

	    // register listener for map state changes
	    // use map controller to zoom in/out, pan and set map center, zoom level etc.
	    mMapController = mMapView.getMapController();
	    mMapView.setBuiltInZoomControls(true, null);
	    
    	if(flag.equals("view"))
    		mMapView.setBuiltInAppControl(true);
	    
	    // create resource provider
	    mMapViewerResourceProvider = new NMapViewerResourceProvider(this);

	    // create overlay manager
	    mOverlayManager = new NMapOverlayManager(this, mMapView, mMapViewerResourceProvider);
	    
	    //말풍선 모양 변경 관련
//	    mOverlayManager.setOnCalloutOverlayListener(new OnCalloutOverlayListener() {
//			@Override
//			public NMapCalloutOverlay onCreateCalloutOverlay(NMapOverlay arg0,
//					NMapOverlayItem arg1, Rect arg2) {
//				
////				return  new NMapCalloutBasicOverlay(arg0, arg1, arg2);
//				return null;
//			}
//		});
	    int markerId = NMapPOIflagType.PIN;
	    
	    // 핀 데이터 추가, 배치
	    NMapPOIdata poiData = new NMapPOIdata(1, mMapViewerResourceProvider);
	    poiData.beginPOIdata(1);
	    NMapPOIitem item;
	    if(flag.equals("view"))
	    	item = poiData.addPOIitem(longi, lati, place, markerId, 0);
	    else
	    	item = poiData.addPOIitem(longi, lati, "검색중...", markerId, 0);
	    
	    if(flag.equals("edit")){
	    	// set floating mode
	    	item.setFloatingMode(NMapPOIitem.FLOATING_TOUCH | NMapPOIitem.FLOATING_DRAG);
	    	// show right button on callout
	    	item.setRightButton(true);
	    }
	    
	    mFloatingPOIitem = item;
	    poiData.endPOIdata();
	    
	    // create POI data overlay
	    NMapPOIdataOverlay poiDataOverlay = mOverlayManager.createPOIdataOverlay(poiData, null);
	    
	    // show all POI data
	    poiDataOverlay.showAllPOIdata(0);
	    
	    poiDataOverlay.setOnFloatingItemChangeListener(new OnFloatingItemChangeListener() {
			@Override
			public void onPointChanged(NMapPOIdataOverlay arg0, NMapPOIitem arg1) {
				NGeoPoint point = arg1.getPoint();
				findPlacemarkAtLocation(point.longitude, point.latitude);
				Log.i("LOG_TAG", "onPointChanged: point=" + point.toString());
			}
		});
	    mFloatingPOIdataOverlay = poiDataOverlay;
	    
	    // 경로 추가
//	    NMapPathData pathData = new NMapPathData(9);
//
//	    pathData.initPathData();
//	    pathData.addPathPoint(127.108099, 37.366034, NMapPathLineStyle.TYPE_SOLID);
//	    pathData.addPathPoint(127.108088, 37.366043, 0);
//	    pathData.addPathPoint(127.108079, 37.365619, 0);
//	    pathData.addPathPoint(127.107458, 37.365608, 0);
//	    pathData.addPathPoint(127.107232, 37.365608, 0);
//	    pathData.addPathPoint(127.106904, 37.365624, 0);
//	    pathData.addPathPoint(127.105933, 37.365621, NMapPathLineStyle.TYPE_DASH);
//	    pathData.addPathPoint(127.105929, 37.366378, 0);
//	    pathData.addPathPoint(127.106279, 37.366380, 0);
//	    pathData.endPathData();
//
//	    NMapPathDataOverlay pathDataOverlay = mOverlayManager.createPathDataOverlay(pathData);
	    
	    // show all path data
//	    pathDataOverlay.showAllPathData(0);
	    
	    // 핀 클릭시, 핀 위에 배너 클릭시 이벤트
	    poiDataOverlay.setOnStateChangeListener(new OnStateChangeListener() {
			@Override
			public void onFocusChanged(NMapPOIdataOverlay arg0, NMapPOIitem arg1) {
				 
			}
			
			@Override
			public void onCalloutClick(NMapPOIdataOverlay arg0, NMapPOIitem arg1) {
				// TODO Auto-generated method stub
				if(flag.equals("edit")){
					map = mMapController.getMapView().getDrawingCache();
					Intent resultIntent = new Intent();
					resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
					resultIntent.putExtra("latitude", arg1.getPoint().latitude);
					resultIntent.putExtra("longitude", arg1.getPoint().longitude);
					resultIntent.putExtra("place", arg1.getTitle().toString());
					setResult(RESULT_OK, resultIntent);
					finish();
					overridePendingTransition(0, 0);
				} else if(flag.equals("view")) {
					mMapView.executeNaverMap();
				}
			}
		});
	    
		mMapController.setMapCenter(new NGeoPoint(longi, lati), 11);
	    
	}
	
	public void onMapInitHandler(NMapView mapView, NMapError errorInfo) {
        if (errorInfo == null) { // success
        	
        } else { // fail
            Log.e("LOG_TAG", "onMapInitHandler: error=" + errorInfo.toString());
        }
	} 
	
	/* NMapDataProvider Listener */
	private final NMapActivity.OnDataProviderListener onDataProviderListener = new NMapActivity.OnDataProviderListener() {

		@Override
		public void onReverseGeocoderResponse(NMapPlacemark placeMark, NMapError errInfo) {

			if (DEBUG) {
				Log.i(LOG_TAG, "onReverseGeocoderResponse: placeMark="
					+ ((placeMark != null) ? placeMark.toString() : null));
			}

			if (errInfo != null) {
				Log.e(LOG_TAG, "Failed to findPlacemarkAtLocation: error=" + errInfo.toString());

				Toast.makeText(NaverMapActivity.this, errInfo.toString(), Toast.LENGTH_LONG).show();
				return;
			}

			if (mFloatingPOIitem != null && mFloatingPOIdataOverlay != null) {
				mFloatingPOIdataOverlay.deselectFocusedPOIitem();

				if (placeMark != null) {
					mFloatingPOIitem.setTitle(placeMark.toString());
				}
				mFloatingPOIdataOverlay.selectPOIitemBy(mFloatingPOIitem.getId(), false);
			}
		}

	};

	
}
