package sg.edu.nus.iss.day28.repository;

import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.TextCriteria;
import org.springframework.data.mongodb.core.query.TextQuery;
import org.springframework.stereotype.Repository;
import static sg.edu.nus.iss.day28.Constants.*; 

@Repository
public class CommentRepo {

    @Autowired
    private MongoTemplate template;

    /*
			db.comments.find(
				{ 
					$text: {
						$search: "words"
					}
				},
				{
					textScore: {
						$meta: "textScore"
					}
				}
			).limit(3)
	 */
    public List<Document> searchCommentText(String... texts) {

		// Print out each text string being searched for
		for (int i = 0; i < texts.length; i++) 
			System.out.printf(">>> texts[%d]: %s\n", i, texts[i]);

		// Create a TextCriteria object for the default language that matches any of the text strings
		TextCriteria criteria = TextCriteria.forDefaultLanguage()
				.matchingAny(texts);

		// Create a TextQuery object with the above criteria, including the score field and sorting by score
		TextQuery textQuery = TextQuery.queryText(criteria)
				// Use this for method chaining
				.includeScore(FIELD_TEXT_SCORE)
				.sortByScore();

		//textQuery.setScoreFieldName(FIELD_TEXT_SCORE);
		// Set a limit of 3 for the number of comments to be returned
		Query query = textQuery.limit(3);

		return template.find(query, Document.class, COLLECTION_COMMENTS);
	}
    
}
