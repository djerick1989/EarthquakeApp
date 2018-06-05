package test.nicaragua.com.earthquakeapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import test.nicaragua.com.earthquakeapp.database.EventRepository;
import test.nicaragua.com.earthquakeapp.model.Event;


public class MapFragment extends Fragment {

    MapView mMapView;
    private static GoogleMap googleMap;
    Context mContext;
    List<Event> eventList;

    public MapFragment() {
    }

    class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        private final View mWindow;

        MyInfoWindowAdapter(){
            mWindow = getActivity().getLayoutInflater().inflate(R.layout.custom_info_window, null);
        }

        @Override
        public View getInfoContents(Marker marker) {

            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        private void render(Marker marker, View view) {
            try {
                String title = marker.getTitle();
                TextView titleUi = ((TextView) view.findViewById(R.id.title));
                if (title != null) {
                    titleUi.setText(title);
                } else {
                    titleUi.setText("");
                }

                String snippet = marker.getSnippet().toLowerCase();
                TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
                if (snippet != null) {
                    snippetUi.setText(snippet);
                } else {
                    snippetUi.setText("");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public static MapFragment newInstance() {
        MapFragment fragment = new MapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        eventList = EventRepository.getAll(mContext);
    }

    public void zoomMap(Event event, Context context) {

        LatLng latLnglocal = new LatLng(event.getLatitude(), event.getLongitude());
        if (event.getMagnitude() < 3) {
            Marker marker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.green_circle)).position(latLnglocal).
                    title(event.getMagnitude().toString()+ " " + context.getResources().getString(R.string.str_magnitude)).snippet(event.getDirection()));
            marker.showInfoWindow();
        } else {
            Marker marker = googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.red_circle)).position(latLnglocal).
                    title(event.getMagnitude().toString() + " " + context.getResources().getString(R.string.str_magnitude)).snippet(event.getDirection()));
            marker.showInfoWindow();
        }
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLnglocal).zoom(11).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition
                (cameraPosition));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mMapView= (MapView) view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;
                try {
                    if (eventList.size() > 0) {
                        for (int i = 0; i < eventList.size(); i++) {
                            try {
                                Event event = eventList.get(i);
                                LatLng latLnglocal = new LatLng(event.getLatitude(), event.getLongitude());
                                if (event.getMagnitude() < 3) {
                                    googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.green_circle)).position(latLnglocal).
                                            title(event.getMagnitude().toString()+ " " + getActivity().getResources().getString(R.string.str_magnitude)).snippet(event.getDirection()));
                                } else {
                                    googleMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.red_circle)).position(latLnglocal).
                                            title(event.getMagnitude().toString() + " " + getActivity().getResources().getString(R.string.str_magnitude)).snippet(event.getDirection()));
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    // For zooming automatically to the location of the marker
                    CameraPosition cameraPosition = new CameraPosition.Builder().zoom(16).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition
                            (cameraPosition));
                } catch (Exception e){
                    e.printStackTrace();
                }

                googleMap.getUiSettings().setCompassEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                googleMap.getUiSettings().setRotateGesturesEnabled(true);
                googleMap.setInfoWindowAdapter(new MyInfoWindowAdapter());

            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
