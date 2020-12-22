package cool.solr.response;

import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

public class FakeServletResponse
    implements HttpServletResponse {

    @Override
    public void addCookie(Cookie cookie) {
    }

    @Override
    public boolean containsHeader(String name) {
        return false;
    }

    @Override
    public String encodeURL(String url) {
        return url;
    }

    @Override
    public String encodeRedirectURL(String url) {
        return url;
    }

    @Override
    public String encodeUrl(String url) {
        return url;
    }

    @Override
    public String encodeRedirectUrl(String url) {
        return url;
    }

    @Override
    public void sendError(int sc, String msg) {

    }

    @Override
    public void sendError(int sc) {

    }

    @Override
    public void sendRedirect(String location) {

    }

    @Override
    public void setDateHeader(String name, long date) {

    }

    @Override
    public void addDateHeader(String name, long date) {

    }

    @Override
    public void setHeader(String name, String value) {

    }

    @Override
    public void addHeader(String name, String value) {

    }

    @Override
    public void setIntHeader(String name, int value) {

    }

    @Override
    public void addIntHeader(String name, int value) {

    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public void setStatus(int sc) {

    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public void setStatus(int sc, String sm) {

    }

    @Override
    public ServletOutputStream getOutputStream() {
        return null;
    }

    @Override
    public int getStatus() {
        return 0;
    }

    @Override
    public PrintWriter getWriter() {
        return null;
    }

    @Override
    public String getHeader(String name) {
        return null;
    }

    @Override
    public void setContentType(String type) {

    }

    @Override
    public Collection<String> getHeaders(String name) {
        return null;
    }

    @Override
    public void setCharacterEncoding(String charset) {

    }

    @Override
    public Collection<String> getHeaderNames() {
        return null;
    }

    @Override
    public void setContentLength(int len) {

    }

    @Override
    public void setContentLengthLong(long len) {

    }


    @Override
    public void setBufferSize(int size) {

    }

    @Override
    public int getBufferSize() {
        return 0;
    }

    @Override
    public void flushBuffer() {

    }

    @Override
    public void resetBuffer() {

    }

    @Override
    public boolean isCommitted() {
        return false;
    }

    @Override
    public void reset() {

    }

    @Override
    public void setLocale(Locale loc) {

    }

    @Override
    public Locale getLocale() {
        return null;
    }

}
