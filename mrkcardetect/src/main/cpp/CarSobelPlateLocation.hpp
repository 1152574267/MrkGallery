#ifndef CarSobelPlateLocation_hpp
#define CarSobelPlateLocation_hpp

class CarSobelPlateLocation : CarPlateLocation {
public:
    CarSobelPlateLocation();

    ~CarSobelPlateLocation();

    void plateLocate(Mat src, vector<Mat> &plates);

private:
    void processMat(Mat src, Mat &dst, int blur_size, int close_w, int close_h);
};

#endif /* CarSobelPlateLocation_hpp */
