var express = require('express');
var router = express.Router();
var db = require('./connectdb')();

/* GET users listing. */
router.get('/userDetails', function(req, res, next) {

    var email = req.query.email;
    console.log(email);

    db.query('select * from user_details where email = ?', [email], function(err, rows, fields) {
        if (err) throw err;


        if (rows.length > 0) {
            res.json({success: "1", user_details: rows[0], message: "user details retrieved"});
        }else {
            res.json({success: "0", message: "error retrieving user details"});
        }
    })

});

router.get('/userReceipts', function (req, res, next) {
    var email = req.query.email;
    console.log(email)
    console.log("Hello email")

    db.query('SELECT receipt_id,store_name,store_address,download_url,distance_traveled_by_user from receipt_details where user_name = ?', [email], function (err, rows, fields) {
        if (err) throw err;


        if (rows.length > 0) {
            console.log(JSON.stringify(rows));
            res.send({success: "1", receipts: rows, message: "user details retrieved"});

        }else {
            res.json({success: "0", message: "error retrieving user details"});
        }
    })
})

module.exports = router;
