package funs.fact.extractor;

import funs.model.ChunkStore;
import funs.model.DocumentStore;
import funs.model.corpus.Document;

/***
 * This interface is the template that fact extractors should adhere to.
 * Facts collected should be stored on the chunks.
 */
public interface FactExtractorInterface {
    public void extract(Document docs);
}
