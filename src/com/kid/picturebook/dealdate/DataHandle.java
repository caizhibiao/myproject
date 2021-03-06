package com.kid.picturebook.dealdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.database.Cursor;

import com.kid.picturebook.db.Contract.BookContentContract;
import com.kid.picturebook.db.DBHelper;
import com.kid.picturebook.db.Contract.PictureBookContract;
import com.kid.picturebook.entity.BookContent;
import com.kid.picturebook.entity.PictureBook;

public class DataHandle {
	private static DataHandle instance;
	private Map<Integer, PictureBook> mMapPictureBook;
	private Map<Integer, List<BookContent>> mMapBookContents;
	
	public static DataHandle getInstance() {
		if(instance == null)
			instance = new DataHandle();
		return instance;
	}
	
	public void initAllPictureBooks() {
		if(mMapPictureBook == null) {
			mMapPictureBook = new HashMap<Integer, PictureBook>();
			DBHelper helper = DBHelper.getInstance();
			Cursor cursor = helper.select(PictureBookContract.TABLE_NAME);
			if(cursor != null) {
				while(cursor.moveToNext()) {
					PictureBook tem = new PictureBook(cursor.getString(cursor.getColumnIndex(PictureBookContract._TITLE)), cursor.getLong(cursor
							.getColumnIndex(PictureBookContract._CREATE_TIME)));
					tem.setId(cursor.getInt(cursor.getColumnIndex(PictureBookContract._ID)));
					mMapPictureBook.put(tem.getId(), tem);
					
				}
			}
			// Iterator<Entry<Integer, PictureBook>> entryKeyIterator =
			// mMapPictureBook.entrySet().iterator();
			// while(entryKeyIterator.hasNext()) {
			// Entry<Integer, PictureBook> e = entryKeyIterator.next();
			// PictureBook value = e.getValue();
			// }
			
			cursor = helper.select(BookContentContract.TABLE_NAME);
			if(cursor != null) {
				while(cursor.moveToNext()) {
					BookContent bookContent = new BookContent(cursor.getInt(cursor.getColumnIndex(BookContentContract._BOOK_ID)));
					bookContent.setPage(cursor.getInt(cursor.getColumnIndex(BookContentContract._PAGE)));
					bookContent.setPath_pic(cursor.getString(cursor.getColumnIndex(BookContentContract._PATH_PIC)));
					bookContent.setPath_bg_audio(cursor.getString(cursor.getColumnIndex(BookContentContract._PATH_BG_AUDIO)));
					bookContent.setPath_click_audio(cursor.getString(cursor.getColumnIndex(BookContentContract._PATH_CLICK_AUDIO)));
					bookContent.setId(cursor.getInt(cursor.getColumnIndex(BookContentContract._ID)));
					if(mMapPictureBook.get(bookContent.getBookId())!=null){
						mMapPictureBook.get(bookContent.getBookId()).addBookContent(0,bookContent);
					}
				}
			}
		}
		
	}
	
	public Map<Integer, PictureBook> getAllBooks() {
		if(mMapPictureBook == null) {
			initAllPictureBooks();
		}
		return mMapPictureBook;
	}
	
	public PictureBook getPictureBookByBookId(int id) {
		return mMapPictureBook.get(id);
	}
	
	public List<BookContent> getBookContentsByBookId(int id) {
		return mMapBookContents.get(id);
	}
	
	public void deletePictureBook(int id) {
		if(mMapPictureBook != null) {
			mMapPictureBook.remove(id);
		}
		DBHelper helper = DBHelper.getInstance();
		helper.delete(PictureBookContract.TABLE_NAME, id);
		helper.delete(BookContentContract.TABLE_NAME, id);
		 
	}
	public void addPictureBook(PictureBook pictureBook) {
		if(mMapPictureBook == null) {
			mMapPictureBook = new HashMap<Integer, PictureBook>();
		}
		if(!mMapPictureBook.containsKey(pictureBook))
			mMapPictureBook.put(pictureBook.getId(), pictureBook);
		
	}
}
