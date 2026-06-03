@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    
    initViews();
    
    dataManager = OBDDataManager.getInstance();
    dataManager.init(this);  // 传入 context
    dataManager.addListener(this);
    
    startTimeUpdate();
}
