#ifndef CarColorPlateLocation_hpp
#define CarColorPlateLocation_hpp

class CarColorPlateLocation : CarPlateLocation {
public:
    CarColorPlateLocation();

    ~CarColorPlateLocation();

    void plateLocate(Mat src, vector<Mat> &sobel_Plates);
};

#endif /* CarColorPlateLocation_hpp */
