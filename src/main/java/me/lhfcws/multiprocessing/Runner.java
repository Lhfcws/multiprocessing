package me.lhfcws.multiprocessing;

import java.io.Serializable;

/**
 * me.lhfcws.multiprocessing.Runner
 *
 * @author lhfcws
 * @since 2017/3/28
 */
public interface Runner extends Serializable {
    /**
     * run
     * @return
     */
    Serializable run() throws Exception;
}


