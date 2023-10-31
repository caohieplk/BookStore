package com.example.bookstore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookstore.Adapter.AdapterBxh;
import com.example.bookstore.Adapter.AdapterSach;
import com.example.bookstore.R;
import com.example.bookstore.dto.ItemSach;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ImageView imgChinhTri, imgCongGiao, imgHomThu, imgPhatGiao, imgSachMoiNhat, imgSachNoi, imgSachTruyen, imgTruyenTranh;
    private RecyclerView recyPodCast, recySachMienPhi, recySach,recyBxh;
    private List<ItemSach> itemSachList;
    public HomeFragment() {
        // tạo biến tham chiếu cái này thì dễ rồi
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        imgChinhTri = view.findViewById(R.id.imgSachChinhTri);
        imgCongGiao = view.findViewById(R.id.imgSachCongGiao);
        imgHomThu = view.findViewById(R.id.imgHopThu);
        imgPhatGiao = view.findViewById(R.id.imgSachPhatGiao);
        imgSachMoiNhat = view.findViewById(R.id.imgSachMoiNhat);
        imgSachNoi = view.findViewById(R.id.imgSachNoi);
        imgSachTruyen = view.findViewById(R.id.imgSachTruyen);
        imgTruyenTranh = view.findViewById(R.id.imgTruyenTranh);
        recyPodCast = view.findViewById(R.id.recypodcast);
        recySachMienPhi = view.findViewById(R.id.recysachmienphi);
        recySach = view.findViewById(R.id.recysachmoi);
        recyBxh = view.findViewById(R.id.recybxh);
        itemSachList = new ArrayList<>();
        // Ánh xạ view để bắt sự kiện
        load();
        imgChinhTri.setOnClickListener(view1 -> loadFragment(new ChinhTriFragment()));
        imgCongGiao.setOnClickListener(view1 -> loadFragment(new CongGiaoFragment()));
        imgHomThu.setOnClickListener(view1 -> loadFragment(new HomThuFragment()));
        imgPhatGiao.setOnClickListener(view1 -> loadFragment(new PhatGiaoFragment()));
        imgSachMoiNhat.setOnClickListener(view1 -> loadFragment(new SachMoiNhatFragment()));
        imgSachNoi.setOnClickListener(view1 -> loadFragment(new SachNoiFragment()));
        imgSachTruyen.setOnClickListener(view1 -> loadFragment(new SachTruyenFragment()));
        imgTruyenTranh.setOnClickListener(view1 -> loadFragment(new TruyenTranhFragment()));
// Sau khi ánh xạ view dùng setOnClickListener khi click vào item sẽ chuyển sang fragment tương ứng
        //Đoạn này vd khi click icon sách nó sẽ chuyển sang layout sách

    }

    public void loadFragment(Fragment fragment) {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment).commit();
    }
//Hàm này là để load fragment mục đích là thay thế fragment hiện tại thành 1 fragment mới có dữ liệu tương ứng

    private void load(){
        itemSachList.add(new ItemSach("Trí Tuệ Nho Thái",R.drawable.image15));
        itemSachList.add(new ItemSach("Đừng lựa chọn khi còn trẻ",R.drawable.image31));
        itemSachList.add(new ItemSach("Trí Tuệ Nho Thái",R.drawable.image15));
        itemSachList.add(new ItemSach("Đừng lựa chọn khi còn trẻ",R.drawable.image31));
        itemSachList.add(new ItemSach("Trí Tuệ Nho Thái",R.drawable.image15));
        itemSachList.add(new ItemSach("Đừng lựa chọn khi còn trẻ",R.drawable.image31));
        itemSachList.add(new ItemSach("Trí Tuệ Nho Thái",R.drawable.image15));
        itemSachList.add(new ItemSach("Đừng lựa chọn khi còn trẻ",R.drawable.image31));
        itemSachList.add(new ItemSach("Trí Tuệ Nho Thái",R.drawable.image15));
        itemSachList.add(new ItemSach("Đừng lựa chọn khi còn trẻ",R.drawable.image31));
// Đây là đoạn list data, được gán vào các dữ liệu là năm và hình ảnh, khi hiển thị lên list view nó sẽ gọi lên các dữ liệu này và hiển thị tương ứng
        List<String> list = new ArrayList<>();
        list.add("Sách hay");
        list.add("Truyện hay");
        list.add("Podcast hay");
        // Sau khi tạo đoạn list data ở trên, chúng ta add nó vào listview bằng List.add
        AdapterSach adapterSach = new AdapterSach(getContext(),itemSachList,HomeFragment.this);
        recySach.setAdapter(adapterSach);
        recyPodCast.setAdapter(adapterSach);
        recySachMienPhi.setAdapter(adapterSach);
        recySach.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recyPodCast.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        recySachMienPhi.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        AdapterBxh adapterBxh = new AdapterBxh(list);
        recyBxh.setAdapter(adapterBxh);
        recyBxh.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
    }
}
//Cuối cùng là Đoạn này dùng để hiển thị các layout fragment ra bên ngoài list view sử dụng thuộc tính HORIZONTAL để có thể vuốt ngang