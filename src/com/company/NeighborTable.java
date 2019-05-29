public class NeighborTable {

    private int nbhoodSeqNum;
    private ConcurrentSkipListSet<Integer> nbSet;
    private ConcurrentSkipListMap<Integer, String> oneHopNbs;
    private ConcurrentSkipListMap<Integer, ConcurrentSkipListSet<Integer>> twoHopNbs;
    private ConcurrentSkipListMap<Integer, Integer> twoHopNbsThroughMPR;
    private ConcurrentSkipListSet<Integer> mprSet;
    private ConcurrentSkipListSet<Integer> mprSelectorTable;


    public NeighborTable() {
        this.nbhoodSeqNum = 0;
        this.oneHopNbs = new ConcurrentSkipListMap<Integer, String>();
        this.twoHopNbs = new ConcurrentSkipListMap<Integer, ConcurrentSkipListSet<Integer>>();
        this.twoHopNbsThroughMPR = new ConcurrentSkipListMap<Integer, Integer>();
        this.mprSelectorTable = new ConcurrentSkipListSet<Integer>();
        this.nbSet = new ConcurrentSkipListSet<Integer>();
    }

    public int getNbhoodSeqNum() {
        return nbhoodSeqNum;
    }

    public ConcurrentSkipListSet<Integer> getNbSet() {
        return nbSet;
    }

    public void updateNbSet() {
        ConcurrentSkipListSet<Integer> biLinks = new ConcurrentSkipListSet<Integer>();
        for (int nid : this.oneHopNbs.keySet()) {
            String linkStatus = oneHopNbs.get(nid);
            if (linkStatus.equalsIgnoreCase("BI") || linkStatus.equalsIgnoreCase("MPR")) {
                biLinks.add(nid);
            }
        }
        this.nbSet = biLinks;
    }

    public ConcurrentSkipListMap<Integer, String> getOneHopNbs() {
        return oneHopNbs;
    }

    public boolean isOneHopNb(int source) {
        return oneHopNbs.containsKey(source);
    }

    public boolean isTwoHopNb(int source) {
        return twoHopNbs.containsKey(source);
    }

    public void removeTwoHopNb(int source) {
        twoHopNbs.remove(source);
    }

    public ConcurrentSkipListMap<Integer, ConcurrentSkipListSet<Integer>> getTwoHopNbs() {
        return twoHopNbs;
    }

    public boolean updateTwoHopNbs(int selfId, int source, ConcurrentSkipListMap<Integer, String> nbsOfSource, boolean updated) {
        for (int twoHopNb : twoHopNbs.keySet()) {
            if (twoHopNbs.get(twoHopNb).contains(source)) {
                if (!nbsOfSource.containsKey(twoHopNb) || (nbsOfSource.get(twoHopNb).equalsIgnoreCase("UNI"))) {
                    twoHopNbs.get(twoHopNb).remove(Integer.valueOf(source));
                    updated = true;
                }
            }
        }
        for (int nid : nbsOfSource.keySet()) {
            if (nid == selfId) {
                if (nbsOfSource.get(selfId).equals("MPR")) {
                    if (!mprSelectorTable.contains(source)) {
                        System.out.println(source + " is not in MPR selector table.");
                        mprSelectorTable.add(source);
                        //this.print
                    }
                } else {
                    if (mprSelectorTable.contains(source)) {
                        System.out.println(source + " is in MPR selector table.");
                        mprSelectorTable.remove(source);
                        //this.print
                    }
                }
            }
            else if (nbsOfSource.get(nid).equalsIgnoreCase("BI") || nbsOfSource.get(nid).equalsIgnoreCase("MPR")) {
                ConcurrentSkipListSet<Integer> accessThroughSet = new ConcurrentSkipListSet<>();
                if (twoHopNbs.containsKey(nid)) {
                    accessThroughSet = twoHopNbs.get(nid);
                }
                boolean added = accessThroughSet.add(source);
                if (added) {
                    updated = true;
                }
                if (!this.isOneHopNb(nid) && !this.isTwoHopNb(nid)) {
                    System.out.println("Node " + nid + " is not in 1-hop neighbor table or 2-hop neighbor table");
                    //this.print
                    twoHopNbs.put(nid, accessThroughSet);
                }
            }
        }
        cleanTwoHopNbs();
        if (updated) {
            updateMPRs();
            ++nbhoodSeqNum;
        }
        return updated;
    }

    public void cleanTwoHopNbs() {
        Set<Entry<Integer, ConcurrentSkipListSet<Integer>>> set = twoHopNbs.entrySet();
        Iterator<Entry<Integer, ConcurrentSkipListSet<Integer>>> iter = set.iterator();
        while (iter.hasNext()) {
            Entry<Integer, ConcurrentSkipListSet<Integer>> entry = iter.next();
            if (entry.getValue().isEmpty()) {
                iter.remove();
            }
        }
    }


    /*
     * link
     */
    public String getLinkStatus(int nid) {
        return oneHopNbs.get(nid);
    }

    public void setLinkStatus(int nid, String status) {
        oneHopNbs.put(nid, status);
        updateNbSet();
    }

    public boolean unlink(int source) {
        if (!oneHopNbs.containsKey(source)) {
            return false;
        }
        oneHopNbs.remove(source);
        updateNbSet();
        for (int twoHopNb : twoHopNbs.keySet()) {
            if (twoHopNbs.get(twoHopNb).contains(source)) {
                twoHopNbs.get(twoHopNb).remove(Integer.valueOf(source));
            }
        }
        cleanTwoHopNbs();
        updateMPRs();
        ++nbhoodSeqNum;
        return true;
    }


    /*
     * access through
     */
    public ConcurrentSkipListSet<Integer> getAccessThrough(int twoHopNb) {
        return twoHopNbs.get(twoHopNb);
    }

    public void setAccessThrough(int twoHopNb, ConcurrentSkipListSet<Integer> accessThroughSet) {
        twoHopNbs.put(twoHopNb, accessThroughSet);
    }


    /*
     * MPR
     */
    public ConcurrentSkipListSet<Integer> getMPRs() {
        return mprSet;
    }

    public ConcurrentSkipListSet<Integer> getMprSelectorTable() {
        return mprSelectorTable;
    }

    public void updateMPRs() {
        twoHopNbsThroughMPR = new ConcurrentSkipListMap(Intger, Integer);
        mprSet = new ConcurrentSkipListSet<Integer>();
        Map<Integer, Integer> accessThroughSizes = new HashMap<Integer, Integer>();
        for (int twoHopHb : twoHopNbs.keySet()) {
            int size = twoHopNbs.get(twoHopHb).size();
            accessThroughSizes.put(twoHopHb, size);
        }

        Set<Entry<Integer, Integer>> set = accessThroughSizes.entrySet();
        ArrayList<Entry<Integer, Integer>> list=  new ArrayList<>(set);
        Collections.sort(list, new Comparator<Map.Entry<Integer, Integer>>() {
            public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                return (o1.getValue().compareTo(o2.getValue()));
            }
        } );

        for (Entry<Integer, Integer> entry : list) {
            int twoHopNb = entry.getKey();
            ConcurrentSkipListSet<Integer> accessThroughSet = twoHopNbs.get(twoHopNb);
            boolean addMPR = true;
            for (int mpr : mprSet) {
                if (accessThroughSet.contains(mpr)) {
                    this.twoHopNbsThroughMPR.put(twoHopNb, mpr);
                    addMPR = false;
                }
            }
            if (addMPR) {
                int mpr = accessThroughSet.first();
                mprSet.add(mpr);
                this.twoHopNbsThroughMPR.put(twoHopNb, mpr);
            }
        }

        for (int nb : this.oneHopNbs.keySet()) {
            if (mprSet.contains(Integer.valueOf(nb))) {
                if (this.oneHopNbs.get(nb).equals("BI")) {
                    this.setLinkStatus(nb, "MPR");
                }
            }
            else {
                if (this.oneHopNbs.get(nb).equals("MPR")) {
                    this.setLinkStatus(nb, "BI");
                }
            }
        }
    }








}














