

      /**
     * Insert image into SD card
     **/

    public static void insertImageInSDCard(Bitmap bp, String name) {

        /** Scale Down **/
        float maxImageSize = 1200;

        float ratio = Math.min(
                (float) maxImageSize / bp.getWidth(),
                (float) maxImageSize / bp.getHeight());
        int width = Math.round((float) ratio * bp.getWidth());
        int height = Math.round((float) ratio * bp.getHeight());

        Bitmap newBp = Bitmap.createScaledBitmap(bp, width, height, true);

        File f = new File(Environment.getExternalStorageDirectory(), Config.MEDIA_FOLDER);
        if (!f.exists()) {
            f.mkdirs();
        }

        File sdCardDirectory = Environment.getExternalStorageDirectory();
        File image = new File(sdCardDirectory.toString(), Config.MEDIA_FOLDER + name + ".jpg");
        if (image.exists()) {
            image.delete();
        }

        // Encode the file as a PNG image.
        FileOutputStream outStream;
        try {
            outStream = new FileOutputStream(image);
            newBp.compress(Bitmap.CompressFormat.JPEG, 60, outStream);
        /* 100 to keep full quality of the image */

            outStream.flush();
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    /**
     * Read image from external memory.
     **/

    public static Bitmap readImageFromExternalMemory(String imageName) throws IOException {
        File file = new File(Environment.getExternalStorageDirectory().toString() + "/.nomedia/appName" + imageName + ".jpg");
        FileInputStream inStream = new FileInputStream(file);

        Bitmap bitmap = BitmapFactory.decodeStream(inStream);
        inStream.close();

        return bitmap;
    }

    /**
    * Scale Down Bitmap
    **/

    public static Bitmap scaledownBitmap(Bitmap bmp, int size) {

            int height = size;

            Bitmap background = Bitmap.createBitmap((int)height, (int)height, android.graphics.Bitmap.Config.ARGB_8888);

            float originalWidth = bmp.getWidth();
            float originalHeight = bmp.getHeight();

            Canvas canvas = new Canvas(background);

            float scale = height / originalWidth;

            float xTranslation = 0.0f;
            float yTranslation = (height - originalHeight * scale) / 2.0f;

            Matrix transformation = new Matrix();
            transformation.postTranslate(xTranslation, yTranslation);
            transformation.preScale(scale, scale);

            Paint paint = new Paint();
            paint.setFilterBitmap(true);

            canvas.drawBitmap(bmp, transformation, paint);

            return background;

    }

    

    public static Bitmap rotateImage(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }






/** Taking Image from Camera **/

    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    takePicture.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFile());
    startActivityForResult(takePicture, image);

    /** With Permission **/
    MarshMallowPermission marshMallowPermission = new MarshMallowPermission(getActivity());
    if(!marshMallowPermission.checkPermissionForCamera()){
        marshMallowPermission.requestPermissionForCamera();
    }else {
        Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePicture.putExtra(MediaStore.EXTRA_OUTPUT, getOutputMediaFile());
        startActivityForResult(takePicture, image);
    }

    /** Image Path Uri **/

    private  Uri getOutputMediaFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = timeStamp + ".jpg";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        pictureImagePath = storageDir.getAbsolutePath() + "/" + imageFileName;
        File file = new File(pictureImagePath);
        return Uri.fromFile(file);
    }




    /** Getting the Image **/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case 1:

                 if(resultCode == RESULT_OK){
                        try {
                            Bitmap bitmap;
                            File imgFile = new  File(pictureImagePath);
                            if(imgFile.exists()){
                                /** For Camera **/
                                bitmap  = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            }else{
                                /** For from Gallery **/
                                Uri pickedImage = data.getData();
                                bitmap = decodeUri(pickedImage);
                            }
                            image2.setImageBitmap(bitmap);
                            isImageView2Set = true;
                            imageSetArray.add("img2");
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                }
                  default:
                break;
        }
    }

    /** Decode Uri **/

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(
                getActivity().getContentResolver().openInputStream(selectedImage), null, o);

        final int REQUIRED_SIZE = 100;

        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(
               getActivity().getContentResolver().openInputStream(selectedImage), null, null);
    }

    /** Stroing Image Example **/
    public void storingImages(){
        Bitmap imageBp;

        int i = 1;
        if (imageSetArray.contains("img1")) {
            imageBp = ((BitmapDrawable) image1.getDrawable()).getBitmap();
            Util.insertImageInSDCard(imageBp, "ImageName" + "_Report" + i);
            i++;
        }
        if (imageSetArray.contains("img2")) {
            imageBp = ((BitmapDrawable) image2.getDrawable()).getBitmap();
            Util.insertImageInSDCard(imageBp, "ImageName" + "_Report" + i);
            i++;
        }
        if (imageSetArray.contains("img3")) {
            imageBp = ((BitmapDrawable) image3.getDrawable()).getBitmap();
            Util.insertImageInSDCard(imageBp, "ImageName" + "_Report" + i);
            i++;
        }
    }
