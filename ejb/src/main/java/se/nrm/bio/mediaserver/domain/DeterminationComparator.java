package se.nrm.bio.mediaserver.domain;

import java.util.Comparator;

/**
 * http://stackoverflow.com/questions/4258700/collections-sort-with-multiple-fields
 * @author ingimar
 */
public class DeterminationComparator implements Comparator<Determination> {

    @Override
    public int compare(Determination d1, Determination d2) {
        if (d1.getSortOrder() > d2.getSortOrder()) {
            return 1;
        } else if (d1.getSortOrder() < d2.getSortOrder()) {
            return -1;

        } else {
            return 1;
        }
    }
    
}
