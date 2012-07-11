// Generated by Enunciate
package org.sonatype.nexus.rest.model;

/**
 *  REST Response object for a list of search results, contains the
 *  typical 'data' parameter, which is a
 *          list of search results.
 *  
 *  @version $Revision$ $Date$
 */
@javax.xml.bind.annotation.XmlType (
  name = "searchNGResponse",
  namespace = ""
)
@javax.xml.bind.annotation.XmlRootElement (
  name = "searchNGResponse",
  namespace = ""
)
public class SearchNGResponse extends org.sonatype.nexus.rest.model.NexusIndexerResponse {

  private int _totalCount;
  private int _from;
  private int _count;
  private boolean _tooManyResults;
  private boolean _collapsed;
  private java.util.List<org.sonatype.nexus.rest.model.NexusNGRepositoryDetail> _repoDetails;
  private java.util.List<org.sonatype.nexus.rest.model.NexusNGArtifact> _data;

  /**
   * The grand total number of results found on index.
   */
  @javax.xml.bind.annotation.XmlElement (
    name = "totalCount",
    namespace = ""
  )
  public int getTotalCount() {
    return this._totalCount;
  }

  /**
   * The grand total number of results found on index.
   */
  public void setTotalCount(int _totalCount) {
    this._totalCount = _totalCount;
  }

  /**
   * The starting index of the results.
   */
  @javax.xml.bind.annotation.XmlElement (
    name = "from",
    namespace = ""
  )
  public int getFrom() {
    return this._from;
  }

  /**
   * The starting index of the results.
   */
  public void setFrom(int _from) {
    this._from = _from;
  }

  /**
   * The number of results in this response.
   */
  @javax.xml.bind.annotation.XmlElement (
    name = "count",
    namespace = ""
  )
  public int getCount() {
    return this._count;
  }

  /**
   * The number of results in this response.
   */
  public void setCount(int _count) {
    this._count = _count;
  }

  /**
   * Flag that states if too many results were found.
   */
  @javax.xml.bind.annotation.XmlElement (
    name = "tooManyResults",
    namespace = ""
  )
  public boolean getTooManyResults() {
    return this._tooManyResults;
  }

  /**
   * Flag that states if too many results were found.
   */
  public void setTooManyResults(boolean _tooManyResults) {
    this._tooManyResults = _tooManyResults;
  }

  /**
   * Flag that states if result set is collapsed, and shows
   * latest versions only.
   */
  @javax.xml.bind.annotation.XmlElement (
    name = "collapsed",
    namespace = ""
  )
  public boolean getCollapsed() {
    return this._collapsed;
  }

  /**
   * Flag that states if result set is collapsed, and shows
   * latest versions only.
   */
  public void setCollapsed(boolean _collapsed) {
    this._collapsed = _collapsed;
  }

  /**
   * Field repoDetails.
   */
  @javax.xml.bind.annotation.XmlElement (
    name = "repositoryDetail",
    namespace = ""
  )
  @javax.xml.bind.annotation.XmlElementWrapper (
    name = "repoDetails",
    namespace = ""
  )
  public java.util.List<org.sonatype.nexus.rest.model.NexusNGRepositoryDetail> getRepoDetails() {
    return this._repoDetails;
  }

  /**
   * Field repoDetails.
   */
  public void setRepoDetails(java.util.List<org.sonatype.nexus.rest.model.NexusNGRepositoryDetail> _repoDetails) {
    this._repoDetails = _repoDetails;
  }

  /**
   * Field data.
   */
  @javax.xml.bind.annotation.XmlElement (
    name = "nexusNGArtifact",
    namespace = ""
  )
  @javax.xml.bind.annotation.XmlElementWrapper (
    name = "data",
    namespace = ""
  )
  public java.util.List<org.sonatype.nexus.rest.model.NexusNGArtifact> getData() {
    return this._data;
  }

  /**
   * Field data.
   */
  public void setData(java.util.List<org.sonatype.nexus.rest.model.NexusNGArtifact> _data) {
    this._data = _data;
  }

}