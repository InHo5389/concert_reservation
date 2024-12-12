let counter = 1;  // 시작값

module.exports = {
  generateUserId: function(context, events, done) {
    context.vars.userId = counter++;  // 1씩 증가
    return done();
  }
};