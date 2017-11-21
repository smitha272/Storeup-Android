/**
 * Created by Krishna.R.K on 11/20/2017.
 */

var express = require('express');
var router = express.Router(),
    db         = require('./connectdb')(),
    formidable = require('formidable'),
    fs         = require('fs-extra'),
    path       = require('path');


router.post('/getCoupon', function(req, res, next) {
    var email    = req.body.email;
    console.log(email);

  /*  SELECT store_name,user_name, COUNT(*)
    FROM storeup.receipt_details
    where user_name = 'krishna@gmail.com'
    GROUP BY store_name;*/
    db.query('select store_name,user_name, COUNT(*) from receipt_details where user_name = ? group by store_name',[email], function(err, rows, fields) {
        if (err) throw err;

        if (rows.length > 0) {
            console.log(rows);
            res.json({success: "1", "receiptCount": rows});
        }else {
            res.json({success: "0", message: "invalid email or password"});
        }
    })
});

module.exports = router;


